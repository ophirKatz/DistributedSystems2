package app.server.leaderelection;

import app.server.leaderelection.nodes.ProcessNode;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;

/**
 * @author Sain Technology Solutions
 */
public class ZooKeeperService {

    private ZooKeeper zooKeeper;
    private ProcessNode.ProcessNodeWatcher processNodeWatcher;

    public ZooKeeperService(final String url, final ProcessNode.ProcessNodeWatcher processNodeWatcher) throws IOException {
        this.zooKeeper = new ZooKeeper(url, 3000, processNodeWatcher);
        this.processNodeWatcher = processNodeWatcher;
    }

    public String createNode(final String node, final boolean watch, final boolean ephimeral) {
        String createdNodePath;
        try {
            final Stat nodeStat = zooKeeper.exists(node, watch);
            if (nodeStat == null) {
                System.out.println("[ZooKeeperService.createNode] Node doesn't exist");
                // createdNodePath = zooKeeper.create(node, new byte[0], Ids.OPEN_ACL_UNSAFE, (ephimeral ? CreateMode.EPHEMERAL_SEQUENTIAL : CreateMode.PERSISTENT));
                createdNodePath = zooKeeper.create(node, new byte[0], Ids.OPEN_ACL_UNSAFE, (ephimeral ? CreateMode.EPHEMERAL_SEQUENTIAL : CreateMode.PERSISTENT));
                zooKeeper.exists(createdNodePath, watch);
            } else {
                System.out.println("[ZooKeeperService.createNode] Node exists");
                createdNodePath = node;
            }
        } catch (KeeperException | InterruptedException e) {
            throw new IllegalStateException(e);
        }

        return createdNodePath;
    }

    public boolean watchNode(final String node, final boolean watch) {
        boolean watched = false;
        try {
            final Stat nodeStat = zooKeeper.exists(node, watch);
            if (nodeStat != null) {
                watched = true;
            }
        } catch (KeeperException | InterruptedException e) {
            throw new IllegalStateException(e);
        }

        return watched;
    }

    public List<String> getChildren(final String node, final boolean watch) {
        List<String> childNodes;
        try {
            childNodes = zooKeeper.getChildren(node, watch);
        } catch (KeeperException | InterruptedException e) {
            throw new IllegalStateException(e);
        }

        return childNodes;
    }
}

