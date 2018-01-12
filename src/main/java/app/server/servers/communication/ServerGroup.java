package app.server.servers.communication;

import app.server.blockchain.Block;
import app.server.servers.jersey.model.AbstractTransaction;
import com.google.gson.Gson;
import org.jgroups.Address;
import org.jgroups.JChannel;
import org.jgroups.Message;
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

    public ServerGroup() throws Exception {
        channel = new JChannel(prop);
        channel.setName(clusterName);
        // channel.addAddressGenerator(ServerProcess.nodeAddressGenerator);
    }

    /*
        public ServerGroup(ReceiverAdapter adapter) throws Exception {
            channel = new JChannel();
            channel.setName(clusterName);
            channel.addAddressGenerator(ServerProcess.nodeAddressGenerator);
            setReceiverAdapter(adapter);
        }
    */
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

    public void publishLeaderAddressToGroup(Address leaderAddress) throws Exception {
        String jsonAddress = "L" + new Gson().toJson(leaderAddress);
        channel.send(new Message(null, jsonAddress));
    }

    public void sendTransactionToLeader(Address leaderAddress, AbstractTransaction transaction) throws Exception {
        String jsonTransaction = transaction.toString();
        channel.send(new Message(leaderAddress, jsonTransaction));
    }

    public void closeGroup() {
        channel.disconnect();
        channel.close();
    }

    public Address getAddress() {
        return channel.getAddress();
    }
}
