package app.server.servers.communication;

import app.server.blockchain.Block;
import app.server.servers.jersey.model.AbstractTransaction;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;

/**
 * Created by ophir on 01/01/18.
 */
public class ServerGroup {
    private static final String props = "/home/ophir/Desktop/studies/semester7/distributed_systems/hw/hw2/DistributedSystems2/src/launch/resources/channel.xml";
    private static final String clusterName = "ServerCluster";

    // The channel all app.server.servers will be connected to.
    private JChannel channel;
    private ReceiverAdapter receiverAdapter;

    public ServerGroup() throws Exception {
        channel = new JChannel(props);
    }

    public ServerGroup(ReceiverAdapter adapter) throws Exception {
        channel = new JChannel(props);
        this.receiverAdapter = adapter;
        channel.setReceiver(adapter);
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
        channel.send(new Message(null, block.toString()));
    }

    public void sendTransactionToLeader(NodeAddress nodeAddress, AbstractTransaction transaction) throws Exception {
        channel.send(new Message(nodeAddress, transaction.toString()));
    }

    public void closeGroup() {
        channel.close();
    }
}
