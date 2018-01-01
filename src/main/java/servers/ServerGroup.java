package servers;

import java.util.Set;

/**
 * Created by ophir on 01/01/18.
 */
public abstract class ServerGroup {

    private final Set<BlockchainProcess> servers;

    public ServerGroup(Set<BlockchainProcess> servers) {
        this.servers = servers;
    }

    // Todo
    /**
     * The algorithm to choose the server to create the next block in the blockchain.
     * */
    public abstract BlockchainProcess nextBlockCreator();
}
