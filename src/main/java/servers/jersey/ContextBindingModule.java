package servers.jersey;

import blockchain.BlockChain;
import com.google.inject.AbstractModule;
import servers.jersey.model.AbstractTransaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ophir on 09/01/18.
 */
public class ContextBindingModule extends AbstractModule {
    public @interface TransactionCache {
    }

    @Override
    protected void configure() {
        // So that all services will have the same blockchain
        BlockChain blockChain = new BlockChain();
        bind(BlockChain.class).toInstance(blockChain);

        // And so that all services will have the same cache
        List<AbstractTransaction> cacheList = new ArrayList<>();
        bind(List.class).annotatedWith(TransactionCache.class).toInstance(cacheList);
    }
}
