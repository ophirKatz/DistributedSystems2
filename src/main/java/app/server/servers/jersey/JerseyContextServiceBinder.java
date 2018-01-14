package app.server.servers.jersey;

import app.server.ServerMain;
import app.server.blockchain.BlockChain;
import app.server.blockchain.TransactionCache;
import app.server.servers.ServerProcess;
import app.server.servers.jersey.services.ContainerService;
import app.server.servers.jersey.services.ShippingService;
import app.server.servers.jersey.services.StorageService;
import org.glassfish.jersey.internal.inject.AbstractBinder;

/**
 * Created by ophir on 10/01/18.
 */
public class JerseyContextServiceBinder extends AbstractBinder {

    private ContainerService containerService;
    private ShippingService shippingService;
    private StorageService storageService;

    public ContainerService getContainerService() {
        return containerService;
    }

    public ShippingService getShippingService() {
        return shippingService;
    }

    public StorageService getStorageService() {
        return storageService;
    }

    @Override
    protected void configure() {
        System.out.println("----    Injecting Dependencies To Servers    ----");
        // So that all services will have the same app.server.blockchain
        BlockChain blockchain = new BlockChain();
        bind(blockchain).to(BlockChain.class);

        // And so that all services will have the same cache
        TransactionCache cache = new TransactionCache();
        bind(cache).to(TransactionCache.class);

        // Also, binding server to services.
        bind(ServerMain.server).to(ServerProcess.class);

        // Binding all services as singletons
        containerService = new ContainerService(blockchain, cache, ServerMain.server);
        bind(containerService).to(ContainerService.class);
        shippingService = new ShippingService(blockchain, cache, ServerMain.server);
        bind(shippingService).to(ShippingService.class);
        storageService = new StorageService(blockchain, cache, ServerMain.server);
        bind(storageService).to(StorageService.class);
    }
}
