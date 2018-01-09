package app.servers.jersey.services;

import app.blockchain.BlockChain;
import app.servers.jersey.model.AbstractTransaction;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    protected List<AbstractTransaction> getAllTransactionsInBlockChainByModelClass(Class<? extends AbstractTransaction> c) {
        final List<AbstractTransaction> allTransactions = new ArrayList<>();
        this.blockChain.getBlocks()
                .forEach(b -> allTransactions.addAll(
                        b.getTransactions()
                                .stream()
                                .filter(t -> t.getClass().equals(c))
                                .collect(Collectors.toList())));
        return allTransactions;
    }
}
