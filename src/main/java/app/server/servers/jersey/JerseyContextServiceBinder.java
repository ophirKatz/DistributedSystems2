package app.server.servers.jersey;

import app.server.ServerMain;
import app.server.blockchain.BlockChain;
import app.server.blockchain.TransactionCache;
import app.server.servers.ServerProcess;
import org.glassfish.jersey.internal.inject.AbstractBinder;

/**
 * Created by ophir on 10/01/18.
 */
public class JerseyContextServiceBinder extends AbstractBinder {
    @Override
    protected void configure() {
        System.out.println("----    Injecting Dependencies To Servers    ----");
        // So that all services will have the same app.server.blockchain
        bind(new BlockChain()).to(BlockChain.class);

        // And so that all services will have the same cache
        TransactionCache cache = new TransactionCache();
        bind(cache).to(TransactionCache.class);

        // Also, binding server to services.
        bind(ServerMain.server).to(ServerProcess.class);
    }
}
