package app.server.leaderelection;

import app.server.leaderelection.nodes.ProcessNode;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.jgroups.Address;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static app.server.leaderelection.nodes.ProcessNode.LEADER_ELECTION_ROOT_NODE;

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
            final Stat nodeStat = zooKeeper.exists(node, false);
            if (nodeStat == null) {
                createdNodePath = zooKeeper.create(node, new byte[0], Ids.OPEN_ACL_UNSAFE, (ephimeral ? CreateMode.EPHEMERAL_SEQUENTIAL : CreateMode.PERSISTENT));
            } else {
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

    public void watchNodes() {
        try {
            for (String nodeSuffix : getChildren(LEADER_ELECTION_ROOT_NODE, true)) {
                String node = LEADER_ELECTION_ROOT_NODE + "/" + nodeSuffix;
                Stat stat = zooKeeper.exists(node, true);
                assert stat != null;
            }
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
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

    public void setNodeData(String nodePath, byte[] nodeData) throws KeeperException, InterruptedException {
        zooKeeper.setData(nodePath, nodeData, -1);
    }

    public Address findLeaderAddressInZookeeper(Class<? extends Address> addressClassObject) throws KeeperException, InterruptedException {
        final List<String> childNodePaths = getChildren(LEADER_ELECTION_ROOT_NODE, false);
        Collections.sort(childNodePaths);
        String leaderNode = LEADER_ELECTION_ROOT_NODE + "/" + childNodePaths.get(0);
        String addressAsJson = new String(zooKeeper.getData(leaderNode, true, zooKeeper.exists(leaderNode, true)));
        try {
            Address address = new Gson().fromJson(addressAsJson, addressClassObject);
            System.out.println(address);
            return address;
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            System.exit(55);
        }
        return null;
    }
}

