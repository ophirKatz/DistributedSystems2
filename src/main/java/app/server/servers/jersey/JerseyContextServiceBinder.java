package app.server.servers.jersey;

import app.server.ServerMain;
import app.server.blockchain.BlockChain;
import app.server.servers.ServerProcess;
import app.server.servers.jersey.model.AbstractTransaction;
import org.glassfish.jersey.internal.inject.AbstractBinder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ophir on 10/01/18.
 */
public class JerseyContextServiceBinder extends AbstractBinder {
    @Override
    protected void configure() {
        // So that all services will have the same app.server.blockchain
        bind(new BlockChain()).to(BlockChain.class);

        // And so that all services will have the same cache
        List<AbstractTransaction> cacheList = new ArrayList<>();
        bind(cacheList).to(List.class);

        // Also, binding server to services.
        bind(ServerMain.server).to(ServerProcess.class);
    }
}
