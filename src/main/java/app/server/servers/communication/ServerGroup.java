package app.server.servers.communication;

import app.server.blockchain.Block;
import app.server.servers.ServerProcess;
import app.server.servers.jersey.model.AbstractTransaction;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;

/**
 * Created by ophir on 01/01/18.
 */
public class ServerGroup {
    public static final String clusterName = "SERVER_CLUSTER";

    // The channel all app.server.servers will be connected to.
    private JChannel channel;
    private ReceiverAdapter receiverAdapter;

    public ServerGroup() throws Exception {
        channel = new JChannel();
        channel.setName(clusterName);
        channel.addAddressGenerator(ServerProcess.nodeAddressGenerator);
    }

    public ServerGroup(ReceiverAdapter adapter) throws Exception {
        channel = new JChannel();
        channel.setName(clusterName);
        channel.addAddressGenerator(ServerProcess.nodeAddressGenerator);
        setReceiverAdapter(adapter);
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
        System.out.println("############ All members of group : ");
        channel.getView().getMembers().forEach(System.out::println);
    }

    public void publishBlockToGroup(Block block) throws Exception {
        channel.send(new Message(null, block.toString()));
    }

    public void sendTransactionToLeader(NodeAddress nodeAddress, AbstractTransaction transaction) throws Exception {
        String jsonTransaction = transaction.toString();
        channel.send(new Message(nodeAddress, jsonTransaction));
    }

    public void closeGroup() {
        channel.disconnect();
        channel.close();
    }
}
