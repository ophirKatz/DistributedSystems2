package blockchain;

import servers.jersey.model.AbstractTransaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Factory class for block's creation.
 * transactions- List of the last transactions that going
 * to be in the block
 */
public class BlockFactory {
    private List<AbstractTransaction> transactions;

    public BlockFactory() {
        this.transactions = new ArrayList<>();
    }

    public Block build(String blockId) {
        return new Block(transactions, blockId);
    }

    public List<AbstractTransaction> getTransactions() {
        return this.transactions;
    }

    public void addTransactionToBlock(AbstractTransaction transaction) {
        this.getTransactions().add(transaction);
    }
}
