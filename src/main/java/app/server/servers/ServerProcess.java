package app.server.servers;

import app.server.ServerMain;
import app.server.blockchain.Block;
import app.server.leaderelection.nodes.ProcessNode;
import app.server.servers.communication.ChannelListener;
import app.server.servers.communication.ServerGroup;
import app.server.servers.jersey.model.AbstractTransaction;
import app.server.servers.jersey.services.AbstractService;
import com.google.gson.Gson;
import org.apache.zookeeper.KeeperException;
import org.jgroups.Address;

/**
 * Created by ophir on 09/01/18.
 */
public class ServerProcess {

    private ServerGroup serverGroup;    // Each server belongs to the server group
    private Address leaderAddress;
    private ProcessNode processNode;

    public Address getAddress() {
        return serverGroup.getAddress();
    }

    public String getAddressAsJson() {
        return new Gson().toJson(serverGroup.getAddress());
    }

    public static AbstractService.LeaderReceiver leaderReceiver;
    public static AbstractService.NonLeaderReceiver nonLeaderReceiver;

    public static ChannelListener channelListener;

    private void setReceiver(boolean isLeader) {
        this.isLeader = isLeader;
        if (isLeader) {
            this.serverGroup.setReceiverAdapter(leaderReceiver);
            if (leaderReceiver == null) {
                this.serverGroup.setReceiverAdapter(new AbstractService.LeaderReceiver(ServerMain.getService()));
            }
        } else {
            this.serverGroup.setReceiverAdapter(nonLeaderReceiver);
            if (nonLeaderReceiver == null) {
                this.serverGroup.setReceiverAdapter(new AbstractService.NonLeaderReceiver(ServerMain.getService()));
            }
        }
    }

    public void setReceiver() {
        setReceiver(isLeader);
    }

    private boolean isLeader;

    public void isLeader(boolean isLeader) {
        serverGroup.isLeader(isLeader);
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
        setReceiver(true);
    }

    /**
     * Called by another process receiving the leader address from the leader
     */
    public void updateLeaderAddress(Address leaderAddress) throws Exception {
        this.leaderAddress = leaderAddress;
        setReceiver(false);
    }

    public ServerProcess(ProcessNode processNode) throws Exception {
        this.processNode = processNode;
        ServerProcess.channelListener = new ChannelListener(this);
        this.serverGroup = new ServerGroup(processNode.getNodePath(), isLeader);
        this.serverGroup.connectToGroup();

        // Here receivers are null since there are no services yet.
        // setReceiver(isLeader);
        this.isLeader = processNode.isLeader();
    }

    public Address findLeaderAddressInZookeeper() throws KeeperException, InterruptedException {
        return processNode.findLeaderAddressInZookeeper();
    }

    public void sendToLeader(AbstractTransaction transaction) throws Exception {
        // setReceiver(false); // todo : this may or may not be the leader
        this.serverGroup.sendTransactionToLeader(findLeaderAddressInZookeeper(), transaction);
    }

    public void distributeBlock(Block block) throws Exception {
        // setReceiver(true);
        this.serverGroup.publishBlockToGroup(block);
    }

    public void leaveGroup() {
        this.serverGroup.closeGroup();
    }
}