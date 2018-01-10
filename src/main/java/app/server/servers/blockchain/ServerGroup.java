package app.server.servers.blockchain;

import java.util.Set;

/**
 * Created by ophir on 01/01/18.
 */
public abstract class ServerGroup {

    private final Set<BlockChainServer> servers;

    public ServerGroup(Set<BlockChainServer> servers) {
        this.servers = servers;
    }

    // Todo

    /**
     * The algorithm to choose the server to create the next block in the app.server.blockchain.
     */
    public abstract BlockChainServer nextBlockCreator();
}
