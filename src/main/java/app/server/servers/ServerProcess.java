package app.server.servers;

import app.server.blockchain.Block;
import app.server.servers.communication.NodeAddress;
import app.server.servers.communication.ServerGroup;
import app.server.servers.jersey.model.AbstractTransaction;
import app.server.servers.jersey.services.AbstractService;
import org.jgroups.Address;
import org.jgroups.stack.AddressGenerator;

/**
 * Created by ophir on 09/01/18.
 */
public class ServerProcess {

    private ServerGroup serverGroup;    // Each server belongs to the server group
    private NodeAddress nodeAddress;

    private NodeAddress leaderNodeAddress;

    public static class NodeAddressGenerator implements AddressGenerator {

        private NodeAddress processNodeAddress;

        public NodeAddressGenerator(String processNodePath) {
            this.processNodeAddress = new NodeAddress(processNodePath);
        }

        @Override
        public Address generateAddress() {
            return processNodeAddress;
        }
    }

    public static NodeAddressGenerator nodeAddressGenerator;

    public static AbstractService.LeaderReceiver leaderReceiver;
    public static AbstractService.NonLeaderReceiver nonLeaderReceiver;

    private void setReceiver() {
        if (isLeader()) {
            this.serverGroup.setReceiverAdapter(leaderReceiver);
        } else {
            this.serverGroup.setReceiverAdapter(nonLeaderReceiver);
        }
    }

    public void updateLeader(String leaderNodePath) {
        this.leaderNodeAddress = new NodeAddress(leaderNodePath);
        setReceiver();
    }

    public NodeAddress getLeaderNodeAddress() {
        return leaderNodeAddress;
    }

    public NodeAddress getNodeAddress() {
        return nodeAddress;
    }

    public ServerProcess(String nodePath, String leaderNodePath) throws Exception {
        ServerProcess.nodeAddressGenerator = new NodeAddressGenerator(nodePath);
        this.serverGroup = new ServerGroup();
        this.nodeAddress = new NodeAddress(nodePath);
        this.leaderNodeAddress = new NodeAddress(leaderNodePath);
        this.serverGroup.connectToGroup();

        setReceiver();
    }

    public void sendToLeader(AbstractTransaction transaction) throws Exception {
        setReceiver();
        this.serverGroup.sendTransactionToLeader(leaderNodeAddress, transaction);
    }

    public void distributeBlock(Block block) throws Exception {
        setReceiver();
        this.serverGroup.publishBlockToGroup(block);
    }

    public boolean isLeader() {
        return nodeAddress.equals(leaderNodeAddress);
    }

    public void leaveGroup() {
        this.serverGroup.closeGroup();
    }
}