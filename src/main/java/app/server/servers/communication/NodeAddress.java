package app.server.servers.communication;

import org.jgroups.Address;

import java.io.DataInput;
import java.io.DataOutput;

/**
 * Created by ophir on 09/01/18.
 */
public class NodeAddress implements Address {
    private final String nodePath;

    public NodeAddress(String nodePath) {
        this.nodePath = nodePath;
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
        return nodePath.length();
    }

    @Override
    public void writeTo(DataOutput dataOutput) throws Exception {
        dataOutput.write(nodePath.getBytes());
    }

    @Override
    public void readFrom(DataInput dataInput) throws Exception {
        dataInput.readFully(nodePath.getBytes());
    }
}
