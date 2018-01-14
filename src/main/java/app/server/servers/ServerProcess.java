package app.server.servers;

import app.server.ServerMain;
import app.server.blockchain.Block;
import app.server.servers.communication.ServerGroup;
import app.server.servers.jersey.model.AbstractTransaction;
import app.server.servers.jersey.services.AbstractService;
import org.jgroups.Address;

/**
 * Created by ophir on 09/01/18.
 */
public class ServerProcess {

    private ServerGroup serverGroup;    // Each server belongs to the server group
    private Address leaderAddress;

    public Address getAddress() {
        return serverGroup.getAddress();
    }

    public static AbstractService.LeaderReceiver leaderReceiver;
    public static AbstractService.NonLeaderReceiver nonLeaderReceiver;

    private void setReceiver(boolean isLeader) {
        this.isLeader = isLeader;
        if (isLeader) {
            this.serverGroup.setReceiverAdapter(leaderReceiver);
            if (leaderReceiver == null) {
                this.serverGroup.setReceiverAdapter(new AbstractService.LeaderReceiver(ServerMain.getService()));
            }
            System.out.println("Setting receiver as LeaderReceiver : " + this.serverGroup.getReceiverAdapter());
        } else {
            this.serverGroup.setReceiverAdapter(nonLeaderReceiver);
            if (nonLeaderReceiver == null) {
                this.serverGroup.setReceiverAdapter(new AbstractService.NonLeaderReceiver(ServerMain.getService()));
            }
            System.out.println("Setting receiver as NonLeaderReceiver : " + this.serverGroup.getReceiverAdapter());
        }
    }

    public void setReceiver() {
        setReceiver(isLeader);
    }

    private boolean isLeader;

    public void isLeader(boolean isLeader) {
        this.isLeader = isLeader;
    }

    public boolean isLeader() {
        return isLeader;
    }

    /**
     * Called by the leader itself
     */
    public void updateLeaderAddress() throws Exception {
        this.leaderAddress = serverGroup.getAddress();
        System.out.println("in updateLeaderAddress() - setting as leader");
        setReceiver(true);
        // TODO : Send to others
        this.serverGroup.publishLeaderAddressToGroup(leaderAddress);
    }

    /**
     * Called by another process receiving the leader address from the leader
     */
    public void updateLeaderAddress(Address leaderAddress) throws Exception {
        this.leaderAddress = leaderAddress;
        System.out.println("in updateLeaderAddress(Address leaderAddress) - setting as non-leader");
        setReceiver(false);
    }

    public ServerProcess(boolean isLeader, String nodePath) throws Exception {
        this.serverGroup = new ServerGroup(nodePath);
        this.serverGroup.connectToGroup();

        // Here receivers are null since there are no services yet.
        // setReceiver(isLeader);
        this.isLeader = isLeader;
    }

    public void sendToLeader(AbstractTransaction transaction) throws Exception {
        // setReceiver(false); // todo : this may or may not be the leader
        this.serverGroup.sendTransactionToLeader(leaderAddress, transaction);
    }

    public void distributeBlock(Block block) throws Exception {
        // setReceiver(true);
        this.serverGroup.publishBlockToGroup(block);
    }

    public void leaveGroup() {
        this.serverGroup.closeGroup();
    }
}