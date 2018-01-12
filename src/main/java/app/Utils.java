package app;

import app.server.servers.communication.NodeAddress;
import org.jgroups.JChannel;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static app.server.servers.communication.ServerGroup.clusterName;

/**
 * Created by ophir on 11/01/18.
 */
public class Utils {

    public static class Ser {
        private String nodePath;
        private String serverHttpPort = "8888";

        public Ser(String path) {
            this.nodePath = path;
        }
    }

    public static String serverPort = "8888";

    public static String findRandomServerPortToConnectTo() throws Exception {
        JChannel channel = new JChannel();
        channel.connect(clusterName);
        List<String> serverPorts = channel.getView().getMembers().stream()
                .map(a -> (NodeAddress) a)
                .filter(NodeAddress::isServerInCluster)
                .map(NodeAddress::getServerHttpPort)
                .collect(Collectors.toList());
        String port = serverPorts.get(new Random().nextInt(serverPorts.size()));
        channel.close();
        return port;
    }
}
