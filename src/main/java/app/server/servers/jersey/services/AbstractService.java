package app.server.servers.jersey.services;

import app.server.blockchain.Block;
import app.server.blockchain.BlockChain;
import app.server.blockchain.TransactionCache;
import app.server.servers.ServerProcess;
import app.server.servers.communication.MessageWithId;
import app.server.servers.jersey.model.AbstractTransaction;
import com.google.gson.Gson;
import org.jgroups.Address;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ophir on 08/01/18.
 */
public abstract class AbstractService<ModelType extends AbstractTransaction> {

    private static int currentBlockId = 0;

    private void blockchainActionsAsLeader(ModelType model) {
        // 2. Add transaction to cache.
        transactionCache.addTransaction(model);

        // 3. If the cache is full, create a block and empty the cache.
        if (transactionCache.isFull()) {
            // Then cache is full - empty it and create block.);
            Block block = new Block(transactionCache.cacheOut(), String.valueOf(currentBlockId++));

            // 4. Insert block to blockchain
            System.out.println("Leader : service.blockChain.addBlock(block)");
            blockChain.addBlock(block);

            // 5. Broadcast the block to other server processes.
            try {
                System.out.println("Leader : service.server.distributeBlock(block)");
                server.distributeBlock(block);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

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
        @SuppressWarnings("unchecked")
        public void receive(Message msg) {
            MessageWithId messageWithId = (MessageWithId) msg;
            if (messageWithId.srcIsDest()) {
                // Ignore self messages
                return;
            }
            System.out.println("Leader : Received Message : [" + msg.getSrc() + "] -> [" + msg.getDest() + "] : [" + msg.getObject() + "]");
            if (msg.getObject().toString().startsWith("L")) {
                Address leaderAddress = new Gson().fromJson(msg.getObject().toString().substring(1), service.server.getAddress().getClass());
                System.out.println("Received leader address : " + leaderAddress.toString());
                System.out.println("JSON rep : " + msg.getObject().toString().substring(1));
                try {
                    System.out.println("Leader : updateLeaderAddress(leaderAddress)");
                    service.server.updateLeaderAddress(leaderAddress);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            }

            // Receiving from another process
            // 1. Parse the transaction
            // todo
            Gson gson = new Gson();
            AbstractTransaction transaction = (AbstractTransaction) gson.fromJson(msg.getObject().toString(), service.modelType);

            service.blockchainActionsAsLeader(transaction);
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
            System.out.println("NonLeader : Received Message : [" + msg.getSrc() + "] -> [" + msg.getDest() + "] : [" + msg.getObject() + "]");
            if (msg.getObject().toString().startsWith("L")) {
                Address leaderAddress = new Gson().fromJson(msg.getObject().toString().substring(1), service.server.getAddress().getClass());
                System.out.println("Received leader address : " + leaderAddress.toString());
                System.out.println("JSON rep : " + msg.getObject().toString().substring(1));

                try {
                    if (!leaderAddress.equals(service.server.getAddress())) {
                        // Only if I am not the leader
                        System.out.println("NonLeader : updateLeaderAddress(leaderAddress)");
                        service.server.updateLeaderAddress(leaderAddress);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            }

            // 2. Parse the block from the message.
            System.out.println("NonLeader : parsing block");
            Block block = Block.parseString(msg.getObject().toString());

            // 3. Insert block to blockchain
            System.out.println("NonLeader : service.blockChain.addBlock(block)");
            service.blockChain.addBlock(block);
        }
    }


    protected BlockChain blockChain;
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

    public void setReceiverForServer() {
        server.setReceiver();
    }

    public void attemptToExpandBlockChain(ModelType model) throws Exception {
        if (server.isLeader()) {
            // If I am the leader, no need to send to leader
            blockchainActionsAsLeader(model);
        } else {
            this.server.sendToLeader(model);
        }
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
