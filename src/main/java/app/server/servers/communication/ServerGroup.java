package app.server.servers.communication;

import app.server.ServerMain;
import app.server.blockchain.Block;
import app.server.servers.ServerProcess;
import app.server.servers.jersey.model.AbstractTransaction;
import org.jgroups.Address;
import org.jgroups.JChannel;
import org.jgroups.ReceiverAdapter;

/**
 * Created by ophir on 01/01/18.
 */
public class ServerGroup {
    public static final String clusterName = "SERVER_CLUSTER";

    private static final String prop = "/home/ophir/Desktop/studies/semester7/distributed_systems/hw/hw2/DistributedSystems2/src/main/resources/tcp.xml";

    // The channel all app.server.servers will be connected to.
    private JChannel channel;
    private ReceiverAdapter receiverAdapter;
    private boolean isLeader;

    public ServerGroup(String nodePath, boolean isLeader) throws Exception {
        channel = new JChannel(prop);
        this.isLeader = isLeader;
        channel.addChannelListener(ServerProcess.channelListener);
    }

    public void setReceiverAdapter(ReceiverAdapter receiverAdapter) {
        this.receiverAdapter = receiverAdapter;
        this.channel.setReceiver(receiverAdapter);
    }

    public ReceiverAdapter getReceiverAdapter() {
        return receiverAdapter;
    }

    public void connectToGroup() throws Exception {
        channel.connect(clusterName);
    }

    public void publishBlockToGroup(Block block) throws Exception {
        channel.send(new MessageWithId(null, block.toString(), ServerMain.getServerId(), false));
    }

    public void sendTransactionToLeader(Address leaderAddress, AbstractTransaction transaction) throws Exception {
        String jsonTransaction = transaction.toString();
        channel.send(new MessageWithId(null, jsonTransaction, ServerMain.getServerId(), true));
    }

    public void closeGroup() {
        channel.disconnect();
        channel.close();
    }

    public Address getAddress() {
        return channel.getAddress();
    }

    public void isLeader(boolean isLeader) {
        this.isLeader = isLeader;
    }
}
