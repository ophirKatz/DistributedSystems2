package app;

import app.server.servers.communication.NodeAddress;
import org.jgroups.JChannel;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static app.server.servers.communication.ServerGroup.clusterName;

/**
 * Created by ophir on 11/01/18.
 */
public class Utils {

    public static long blockSizeThreshold = 1;

    public static class Ser {
        private String nodePath;
        private String serverHttpPort = "8888";

        public Ser(String path) {
            this.nodePath = path;
        }
    }

    public static String serverPort = "8888";
    public static final List<String> serverPorts = new ArrayList<>();

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

    private static String shipChainConfFileName = "/home/ophir/Desktop/studies/semester7/distributed_systems/hw/hw2/" +
            "DistributedSystems2/src/main/resources/shipchain_conf.json";

    public static void readApplicationConfiguration() throws IOException, ParseException {
        JSONObject confObject = (JSONObject) new JSONParser().parse(new FileReader(shipChainConfFileName));
        Utils.blockSizeThreshold = (Long) confObject.get("block_size");
        JSONArray ports = (JSONArray) confObject.get("server_ports");
        for (Object port : ports) {
            serverPorts.add(port.toString());
        }
    }
}
