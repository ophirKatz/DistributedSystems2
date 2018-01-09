package app.servers.communication;

import com.sun.istack.Nullable;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;

import java.util.function.Consumer;

/**
 * Created by ophir on 01/01/18.
 */
public class ServerGroup {
    private static final String props = "/home/ophir/Desktop/studies/semester7/distributed_systems/hw/hw2/DistributedSystems2/src/main/resources/channel.xml";
    private static final String clusterName = "ServerCluster";

    // The channel all app.servers will be connected to.
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

    // Default behaviour
    public void setReceiverLogic(NodeAddress nodeAddress, @Nullable Consumer<Message> consumer) {
        receiverAdapter = new ReceiverAdapter() {
            @Override
            public void receive(Message msg) {
                if (msg.getSrc().equals(nodeAddress)) {
                    return; // If this server send the message, then ignore it.
                }
                if (consumer == null) {
                    // Default behaviour
                    System.out.println("received msg from " + msg.getSrc() + ": " + msg.getObject());
                } else {
                    consumer.accept(msg);
                }
            }
        };
        channel.setReceiver(receiverAdapter);
    }

    public void connectToGroup() throws Exception {
        channel.connect(clusterName);
    }

    public void sendMessageToGroup(Object object) throws Exception {
        channel.send(new Message(null, object));
    }

    public void sendMessageToServer(NodeAddress nodeAddress, Object object) throws Exception {
        channel.send(new Message(nodeAddress, object));
    }

    public void closeGroup() {
        channel.close();
    }
}
