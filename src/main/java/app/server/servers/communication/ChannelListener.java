package app.server.servers.communication;

import app.server.servers.ServerProcess;
import org.jgroups.JChannel;

/**
 * Created by ophir on 14/01/18.
 */
public class ChannelListener implements org.jgroups.ChannelListener {

    private ServerProcess server;

    public ChannelListener(ServerProcess server) {
        this.server = server;
    }


    @Override
    public void channelConnected(JChannel jChannel) {

    }

    @Override
    public void channelDisconnected(JChannel jChannel) {

    }

    @Override
    public void channelClosed(JChannel jChannel) {

    }
}
