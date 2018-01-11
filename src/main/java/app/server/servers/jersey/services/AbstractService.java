package app.server.servers.jersey.services;

import app.server.blockchain.Block;
import app.server.blockchain.BlockChain;
import app.server.blockchain.TransactionCache;
import app.server.servers.ServerProcess;
import app.server.servers.jersey.model.AbstractTransaction;
import com.google.gson.Gson;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ophir on 08/01/18.
 */
public abstract class AbstractService<ModelType extends AbstractTransaction> {

    private static final int cacheThreshold = 5;
    private static int currentBlockId = 0;

    public static class LeaderReceiver extends ReceiverAdapter {

        private final AbstractService service;

        public LeaderReceiver(AbstractService service) {
            this.service = service;
        }

        /**
         * If a leader process receives a message from another server process that contains
         * information about the transaction -> then it adds the transaction to its transaction
         * cache -> then checks if the cache is full -> if so, creates a block (holding the transactions
         * in the cache) -> broadcasts the block to all other server processes and inserts the block to
         * its blockchain.
         * <p>
         * If a leader process receives a message from itself, then it ignores it.
         */
        @Override
        public void receive(Message msg) {
            if (msg.getSrc().equals(service.server.getNodeAddress())) {
                // Ignore
            } else {
                // Receiving from another process
                // 1. Parse the transaction
                // todo
                Gson gson = new Gson();
                AbstractTransaction transaction = (AbstractTransaction) gson.fromJson(msg.getObject().toString(), service.modelType);

                // 2. Add transaction to cache.
                service.transactionCache.addTransaction(transaction);

                // 3. If the cache is full, create a block and empty the cache.
                if (service.transactionCache.isFull()) {
                    // Then cache is full - empty it and create block.
                    Block block = new Block(service.transactionCache.cacheOut(), String.valueOf(currentBlockId++));

                    // 4. Insert block to blockchain
                    service.blockChain.addBlock(block);

                    // 5. Broadcast the block to other server processes.
                    try {
                        service.server.distributeBlock(block);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }

    public static class NonLeaderReceiver extends ReceiverAdapter {

        private final AbstractService service;

        public NonLeaderReceiver(AbstractService service) {
            this.service = service;
        }

        /**
         * If a server process receives a message, then it must be from the leader.
         * It should receive (and parse the message as) a block containing transactions.
         * It should insert the block to the blockchain
         */
        @Override
        public void receive(Message msg) {
            // 1. Assert that truly the leader sent the message. For testing...
            assert msg.getSrc().equals(service.server.getLeaderNodeAddress());

            // 2. Parse the block from the message.
            Block block = Block.parseString(msg.getObject().toString());

            // 3. Insert block to blockchain
            service.blockChain.addBlock(block);
        }
    }



    protected BlockChain blockChain;
    //protected List<AbstractTransaction> transactionCache;
    protected TransactionCache transactionCache;
    protected ServerProcess server;
    private Class<? extends AbstractTransaction> modelType;

    protected AbstractService(BlockChain blockChain, TransactionCache cache, ServerProcess server, Class<? extends AbstractTransaction> modelType) {
        this.blockChain = blockChain;
        this.transactionCache = cache;
        this.server = server;
        this.modelType = modelType;

        this.setReceiversForServerProcess();
    }

    public void setReceiversForServerProcess() {
        // Link server to receivers
        ServerProcess.leaderReceiver = new LeaderReceiver(this);
        ServerProcess.nonLeaderReceiver = new NonLeaderReceiver(this);
    }

    public void attemptToExpandBlockChain(ModelType model) throws Exception {
        this.server.sendToLeader(model);
    }

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
