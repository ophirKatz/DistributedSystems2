package servers.jersey.services;

import blockchain.BlockChain;
import servers.jersey.model.AbstractTransaction;

import java.util.List;

/**
 * Created by ophir on 08/01/18.
 */
public abstract class AbstractService<ModelType extends AbstractTransaction> {

    protected BlockChain blockChain;
    protected List<AbstractTransaction> transactionCache;

    protected AbstractService(BlockChain blockChain, List<AbstractTransaction> cache) {
        this.blockChain = blockChain;
        this.transactionCache = cache;
    }

    public abstract void insertModelToBlockChain(ModelType model);
}
