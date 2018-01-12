package app.server.servers;

import app.server.blockchain.Block;
import app.server.servers.communication.NodeAddress;
import app.server.servers.communication.ServerGroup;
import app.server.servers.jersey.model.AbstractTransaction;
import app.server.servers.jersey.services.AbstractService;
import org.jgroups.Address;

/**
 * Created by ophir on 09/01/18.
 */
public class ServerProcess {

    private ServerGroup serverGroup;    // Each server belongs to the server group
    private NodeAddress nodeAddress;

    private NodeAddress leaderNodeAddress;
    private Address leaderAddress;

    public Address getAddress() {
        return serverGroup.getAddress();
    }

    /*public static class NodeAddressGenerator implements AddressGenerator {

        private NodeAddress processNodeAddress;

        public NodeAddressGenerator(String processNodePath) {
            this.processNodeAddress = new NodeAddress(processNodePath);
        }

        @Override
        public Address generateAddress() {
            return processNodeAddress;
        }
    }*/

    //public static NodeAddressGenerator nodeAddressGenerator;

    public static AbstractService.LeaderReceiver leaderReceiver;
    public static AbstractService.NonLeaderReceiver nonLeaderReceiver;

    private void setReceiver(boolean isLeader) {
        if (isLeader) {
            this.serverGroup.setReceiverAdapter(leaderReceiver);
        } else {
            this.serverGroup.setReceiverAdapter(nonLeaderReceiver);
        }
    }

    /*public void updateLeader(String leaderNodePath) {
        this.leaderNodeAddress = new NodeAddress(leaderNodePath);
        setReceiver(isLeader);
    }*/

    /**
     * Called by the leader itself
     */
    public void updateLeaderAddress() throws Exception {
        this.leaderAddress = serverGroup.getAddress();
        setReceiver(true);
        // TODO : Send to others
        this.serverGroup.publishLeaderAddressToGroup(leaderAddress);
    }

    /**
     * Called by another process receiving the leader address from the leader
     */
    public void updateLeaderAddress(Address leaderAddress) throws Exception {
        this.leaderAddress = leaderAddress;
        setReceiver(false);
    }

    public NodeAddress getLeaderNodeAddress() {
        return leaderNodeAddress;
    }

    public NodeAddress getNodeAddress() {
        return nodeAddress;
    }

    public ServerProcess(boolean isLeader) throws Exception {
        // ServerProcess.nodeAddressGenerator = new NodeAddressGenerator(nodePath);
        this.serverGroup = new ServerGroup();
        // this.nodeAddress = new NodeAddress(nodePath);
        // this.leaderNodeAddress = new NodeAddress(leaderNodePath);
        this.serverGroup.connectToGroup();

        setReceiver(isLeader);
    }

    public void sendToLeader(AbstractTransaction transaction) throws Exception {
        setReceiver(false);
        this.serverGroup.sendTransactionToLeader(leaderAddress, transaction);
    }

    public void distributeBlock(Block block) throws Exception {
        setReceiver(true);
        this.serverGroup.publishBlockToGroup(block);
    }

    public boolean isLeader() {
        return nodeAddress.equals(leaderAddress);
    }

    public void leaveGroup() {
        this.serverGroup.closeGroup();
    }
}