package app.server.servers.communication;

import app.server.ServerMain;
import org.jgroups.Address;
import org.jgroups.Message;

/**
 * Created by ophir on 14/01/18.
 */
public class MessageWithId extends Message {

    private int senderId;

    public MessageWithId(Address address, byte[] msg, int senderId) {
        super(address, msg);
        this.senderId = senderId;
    }

    public MessageWithId(Address address, String msg, int senderId) {
        super(address, msg);
        this.senderId = senderId;
    }

    public boolean srcIsDest() {
        return senderId == ServerMain.getServerId();
    }
}
