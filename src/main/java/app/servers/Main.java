package app.servers;

import app.servers.communication.NodeAddress;
import app.servers.communication.ServerGroup;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;

import java.io.IOException;
import java.net.InetSocketAddress;

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
                //case1- current server is the leader
                if ((nodeAddress.getNodePath()).equals(leaderNodeAddress.getNodePath())) {

                }
                //case2- current server isn't the leader
                else {
                    try {
                        //TODO:Create transaction- of which type
                        sendToLeader(msg.getObject());
                    } catch (Exception e) {
                        System.out.println("Failed to send message to Leader");
                    }
                }

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

    public static void serverMain(String nodePath, String leaderNodePath, int port) {
        try {
            // Setup server
            Server server = new Server(nodePath, leaderNodePath);
            server.publishNodeAddress();
            ReceiverAdapter receiverAdapter = new ReceiverAdapter();

            // Setup http server
            HttpServer httpServer = HttpServer.create(new InetSocketAddress(port), 0);
            httpServer.createContext(nodePath, new HttpHandler() {
                @Override
                public void handle(HttpExchange httpExchange) throws IOException {
                    receiverAdapter.receive(new Message(server.nodeAddress, httpExchange));
                }
            });
            httpServer.setExecutor(null); // creates a default executor
            httpServer.start();

            // listening... and accepting http messages through the http server

            // http server directs message to Resource classes

            // Resource classes direct them to the services which call the Server class's method accordingly.

            //server.leaveGroup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
