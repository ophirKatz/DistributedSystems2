package app.server.servers.communication;

import app.server.ServerMain;
import org.jgroups.Address;
import org.jgroups.Message;

/**
 * Created by ophir on 14/01/18.
 */
public class MessageWithId extends Message {

    private int senderId;
    private boolean toLeader;

    public MessageWithId(Address address, byte[] msg, int senderId, boolean toLeader) {
        super(address, msg);
        this.senderId = senderId;
        this.toLeader = toLeader;
    }

    public MessageWithId(Address address, String msg, int senderId, boolean toLeader) {
        super(address, msg);
        this.senderId = senderId;
        this.toLeader = toLeader;
    }

    public boolean srcIsDest() {
        return senderId == ServerMain.getServerId();
    }

    public boolean isToLeader() {
        return toLeader;
    }

    public void sender() {
        System.out.println("Sender : " + senderId);
    }
}
