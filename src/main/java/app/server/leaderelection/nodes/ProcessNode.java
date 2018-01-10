package app.server.leaderelection.nodes;

import app.server.leaderelection.ZooKeeperService;
import app.server.servers.ServerProcess;
import org.apache.log4j.Logger;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;

import java.io.IOException;
import java.util.Collections;
import java.util.List;


/**
 * Each Process Node belongs to a certain server.
 * Each server knows [via the field leaderNodePath] who the leader
 */
public class ProcessNode implements Runnable {

    private static final Logger LOG = Logger.getLogger(ProcessNode.class);

    private static final String LEADER_ELECTION_ROOT_NODE = "/election";
    private static final String PROCESS_NODE_PREFIX = "/p_";

    private final int id;
    private final ZooKeeperService zooKeeperService;

    private String processNodePath;
    private String watchedNodePath;
    private String leaderNodePath;

    private ServerProcess server;
    private String nodePath;

    public ServerProcess getServer() {
        return server;
    }

    public void setServer(ServerProcess server) {
        this.server = server;
    }

    public String getNodePath() {
        return nodePath;
    }

    public String getLeaderNodePath() {
        return leaderNodePath;
    }


    public ProcessNode(final int id, final String zkURL) throws IOException {
        this.id = id;
        zooKeeperService = new ZooKeeperService(zkURL, new ProcessNodeWatcher());
    }

    private void attemptForLeaderPosition(boolean updateLeader) throws KeeperException, InterruptedException {
        final List<String> childNodePaths = zooKeeperService.getChildren(LEADER_ELECTION_ROOT_NODE, false);
        Collections.sort(childNodePaths);

        int index = childNodePaths.indexOf(processNodePath.substring(processNodePath.lastIndexOf('/') + 1));
        if (index == 0) {
            // I am the leader - update in ServerProcess.class
            System.out.println("[Process: " + id + "] I am the new leader!");

            leaderNodePath = processNodePath;
            if (updateLeader) {
                server.updateLeader(leaderNodePath);
            } // else - server not initialized
        } else {
            // I am not the leader - update in ServerProcess.class
            final String watchedNodeShortPath = childNodePaths.get(index - 1);
            watchedNodePath = LEADER_ELECTION_ROOT_NODE + "/" + watchedNodeShortPath;
            System.out.println("[Process: " + id + "] - Setting watch on node with path: " + watchedNodePath);
            zooKeeperService.watchNode(watchedNodePath, true);

            leaderNodePath = childNodePaths.get(0);
            if (updateLeader) {
                server.updateLeader(leaderNodePath);
            }
        }
    }

    public void run() {
        System.out.println("Process with id: " + id + " has started!");

        final String rootNodePath = zooKeeperService.createNode(LEADER_ELECTION_ROOT_NODE, false, false);
        if (rootNodePath == null) {
            throw new IllegalStateException("Unable to create/access leader election root node with path: " + LEADER_ELECTION_ROOT_NODE);
        }

        processNodePath = zooKeeperService.createNode(rootNodePath + PROCESS_NODE_PREFIX, false, true);
        if (processNodePath == null) {
            throw new IllegalStateException("Unable to create/access process node with path: " + LEADER_ELECTION_ROOT_NODE);
        }

        System.out.println("[Process: " + id + "] Process node created with path: " + processNodePath);

        try {
            // When we are just starting, the server isn't build yet. No leader field to update.
            attemptForLeaderPosition(false);
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public class ProcessNodeWatcher implements Watcher {
        /**
         * TODO : When we are updating the leader, it must be updated in the ServerProcess's class.
         */


        public void process(WatchedEvent event) {
            System.out.println("[Process: " + id + "] Event received: " + event);

            final EventType eventType = event.getType();
            if (EventType.NodeDeleted.equals(eventType)) {
                if (event.getPath().equalsIgnoreCase(watchedNodePath)) {
                    try {
                        // When a process has dies, we do want to update the leader of the server group
                        attemptForLeaderPosition(true);
                    } catch (KeeperException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
