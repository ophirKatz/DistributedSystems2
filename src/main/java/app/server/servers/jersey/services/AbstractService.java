package app.server.servers.jersey.services;

import app.Utils;
import app.server.blockchain.Block;
import app.server.blockchain.BlockChain;
import app.server.blockchain.TransactionCache;
import app.server.servers.ServerProcess;
import app.server.servers.communication.MessageWithId;
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
            System.out.println("Leader : Received Message : [" + msg.getSrc() + "] -> [" + msg.getDest() + "] : [" + msg.getObject() + "]");
            MessageWithId messageWithId = null;
            if (msg.getClass().equals(MessageWithId.class)) {
                messageWithId = (MessageWithId) msg;
            }
            if (messageWithId != null && messageWithId.srcIsDest()) {
                // Ignore self messages
                System.out.println("[Leader] : srcIsDst.  Exiting...");
                return;
            }
            if (messageWithId != null && !messageWithId.isToLeader()) {
                System.out.println("[Leader] : !messageWithId.isToLeader(). Exiting...");
                // Ignore messages that are not for the leader
                return;
            }

            // Receiving from another process
            // 1. Parse the transaction
            // todo
            String msgData = msg.getObject().toString();
            Gson gson = new Gson();
            Class<? extends AbstractTransaction> cls = Utils.getTransactionClassObjectFromString(msgData);

            //}
            System.out.println("msgData : " + msgData + " Type : " + cls.getSimpleName());
            AbstractTransaction transaction = gson.fromJson(msgData, cls);


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
            if (msg.getClass().equals(MessageWithId.class) && ((MessageWithId) msg).isToLeader()) {
                // Ignore messages that are for the leader
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
            System.out.println("Expanding block");
            blockchainActionsAsLeader(model);
        } else {
            System.out.println("Sending to leader instead of expanding block");
            this.server.sendToLeader(model);
        }
    }

    protected List<AbstractTransaction> getAllTransactionsInBlockChainByModelClass(Class<? extends AbstractTransaction> c) {
        final List<AbstractTransaction> allTransactions = new ArrayList<>();
        this.blockChain.getBlocks()
                .forEach(b -> allTransactions.addAll(
                        b.<ModelType>getTransactions()
                                .stream()
                                .filter(t -> t.getClass().equals(c))
                                .collect(Collectors.toList())));
        return allTransactions;
    }
}
