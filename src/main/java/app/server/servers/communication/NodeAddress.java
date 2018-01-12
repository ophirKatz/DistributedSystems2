package app.server.servers.communication;

import app.Utils;
import com.google.gson.Gson;
import org.jgroups.Address;
import org.jgroups.conf.ClassConfigurator;

import java.io.DataInput;
import java.io.DataOutput;

/**
 * Created by ophir on 09/01/18.
 */
public class NodeAddress implements Address {

    static {
        ClassConfigurator.add((short) 5555, NodeAddress.class);
    }

    private String nodePath;
    private String serverHttpPort;

    public static final String defaultNodePath = "/election/";
    public static int serializedSize = 0;

    public NodeAddress() {
        this.nodePath = defaultNodePath;
    }

    public String getServerHttpPort() {
        return serverHttpPort;
    }

    public boolean isServerInCluster() {
        return !this.nodePath.equals(defaultNodePath);
    }

    public NodeAddress(String nodePath) {
        this.nodePath = nodePath;
        this.serverHttpPort = Utils.serverPort;
    }

    public String getNodePath() {
        return nodePath;
    }

    @Override
    public int compareTo(Address o) {
        return nodePath.compareTo(((NodeAddress) o).nodePath);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!obj.getClass().equals(NodeAddress.class)) return false;
        NodeAddress nodeAddress = (NodeAddress) obj;
        return this.nodePath.equals(nodeAddress.nodePath);
    }

    @Override
    public int serializedSize() {
        return NodeAddress.serializedSize;
    }

    @Override
    public void writeTo(DataOutput dataOutput) throws Exception {
        dataOutput.write(new Gson().toJson(this).getBytes());
    }

    @Override
    public void readFrom(DataInput dataInput) throws Exception {
        byte[] bytes = new byte[NodeAddress.serializedSize];
        dataInput.readFully(bytes);
        NodeAddress address = new Gson().fromJson(new String(bytes), this.getClass());
        this.nodePath = address.nodePath;
        this.serverHttpPort = address.serverHttpPort;
    }

    @Override
    public String toString() {
        return nodePath;
    }
}
