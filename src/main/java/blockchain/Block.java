package blockchain;

import transactions.AbstractTransaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anat ana on 07/01/2018.
 */


public class Block {

    private List<AbstractTransaction> transactions;
    private long timestamp;
    private String blockId;
    /*optional:
    * Set<AbstractTransaction> pool;
    * and information about previous transactions
    * */

    public Block(String blockId) {
        this.transactions = new ArrayList<>();
        this.timestamp = System.currentTimeMillis();
        this.blockId = blockId;
    }

    public Block(List<AbstractTransaction> transactions, String blockId) {
        this.transactions = transactions;
        timestamp = System.currentTimeMillis();
        this.blockId = blockId;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public List<AbstractTransaction> getTransactions() {
        return this.transactions;
    }

    public String getBlockId() {
        return this.blockId;
    }

    public AbstractTransaction getTransactionById(Block block, String id) {
        for (AbstractTransaction transaction : block.getTransactions()) {
            if (transaction.getItemID().equals(id)) {
                return transaction;
            }
        }
        return null;
    }

    public void addTransactionToBlock(AbstractTransaction transaction) {
        this.getTransactions().add(transaction);
    }

}
