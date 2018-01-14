package app.server.servers.communication;

import app.server.ServerMain;
import app.server.blockchain.Block;
import app.server.servers.jersey.model.AbstractTransaction;
import com.google.gson.Gson;
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

    public ServerGroup(String nodePath) throws Exception {
        channel = new JChannel(prop);
        channel.setName(nodePath);
        /*channel.addChannelListener(new ChannelListener() {
            @Override
            public void channelConnected(JChannel jChannel) {

            }

            @Override
            public void channelDisconnected(JChannel jChannel) {

            }

            @Override
            public void channelClosed(JChannel jChannel) {

            }
        });*/
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
        System.out.println("Sending block content to group : " + block.toString());
        channel.send(new MessageWithId(null, block.toString(), ServerMain.getServerId()));
    }

    public void publishLeaderAddressToGroup(Address leaderAddress) throws Exception {
        String jsonAddress = "L" + new Gson().toJson(leaderAddress);
        System.out.println(jsonAddress);
        System.out.println("Publishing leader address to group : " + jsonAddress);
        channel.send(new MessageWithId(null, jsonAddress, ServerMain.getServerId()));
    }

    public void sendTransactionToLeader(Address leaderAddress, AbstractTransaction transaction) throws Exception {
        String jsonTransaction = transaction.toString();
        System.out.println("Sending transaction to leader at [" + leaderAddress + "] : " + jsonTransaction);
        System.out.println("Receiver is : " + receiverAdapter.getClass().getSimpleName());
        channel.send(new MessageWithId(leaderAddress, jsonTransaction, ServerMain.getServerId()));
    }

    public void closeGroup() {
        channel.disconnect();
        channel.close();
    }

    public Address getAddress() {
        return channel.getAddress();
    }
}
