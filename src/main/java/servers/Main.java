package servers;

import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import servers.communication.NodeAddress;
import servers.communication.ServerGroup;

/**
 * Created by ophir on 09/01/18.
 */
public class Main {

    public static class Server implements Runnable {
        private ServerGroup serverGroup;    // Each server belongs to the server group
        private NodeAddress nodeAddress;
        private NodeAddress leaderNodeAddress;

        private Object receivedData;

        public Server(String nodePath, String leaderNodePath) throws Exception {
            this.serverGroup = new ServerGroup();
            this.nodeAddress = new NodeAddress(nodePath);
            this.leaderNodeAddress = new NodeAddress(leaderNodePath);
            this.serverGroup.setReceiverLogic(nodeAddress, null);
            this.serverGroup.connectToGroup();
        }

        @Override
        public void run() {

        }

        public void publishNodeAddress() throws Exception {
            assert nodeAddress != null;
            // Publish server's node path to all other servers in the group.
            ReceiverAdapter receiverAdapter = serverGroup.getReceiverAdapter();
            serverGroup.setReceiverAdapter(new ReceiverAdapter() {
                @Override
                public void receive(Message msg) {
                    Server.this.receivedData = msg.getObject();
                }
            });
            this.serverGroup.sendMessageToGroup(nodeAddress);
            serverGroup.setReceiverAdapter(receiverAdapter);
        }

        public void sendToLeader(Object object) throws Exception {
            this.serverGroup.sendMessageToServer(leaderNodeAddress, object);
        }

        public void distributeData(Object object) throws Exception {
            this.serverGroup.sendMessageToGroup(object);
        }

        public boolean isLeader() {
            return nodeAddress.equals(leaderNodeAddress);
        }
    }

    public static void serverMain(String nodePath, String leaderNodePath) {
        try {
            Server server = new Server(nodePath, leaderNodePath);
            server.publishNodeAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
