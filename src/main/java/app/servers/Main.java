package app.servers;

import app.servers.communication.NodeAddress;
import app.servers.communication.ServerGroup;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;

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

        private final ReceiverAdapter saveDataReceiverAdapter = new ReceiverAdapter() {
            @Override
            public void receive(Message msg) {
                Server.this.receivedData = msg.getObject();
            }
        };

        public void publishNodeAddress() throws Exception {
            assert nodeAddress != null;
            // Publish server's node path to all other app.servers in the group.
            ReceiverAdapter receiverAdapter = serverGroup.getReceiverAdapter();
            serverGroup.setReceiverAdapter(saveDataReceiverAdapter);
            this.serverGroup.sendMessageToGroup(nodeAddress);
            serverGroup.setReceiverAdapter(receiverAdapter);
        }

        public void sendToLeader(Object object) throws Exception {
            // Save data
            this.serverGroup.sendMessageToServer(leaderNodeAddress, object);
        }

        public void distributeData(Object object) throws Exception {
            this.serverGroup.sendMessageToGroup(object);
        }

        public boolean isLeader() {
            return nodeAddress.equals(leaderNodeAddress);
        }

        public void leaveGroup() {
            this.serverGroup.closeGroup();
        }
    }

    public static void serverMain(String nodePath, String leaderNodePath) {
        try {
            // Setup server
            Server server = new Server(nodePath, leaderNodePath);
            server.publishNodeAddress();

            // Setup http server

            // listening... and accepting http messages through the http server

            // http server directs message to Resource classes

            // Resource classes direct them to the services which call the Server class's method accordingly.

            server.leaveGroup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
