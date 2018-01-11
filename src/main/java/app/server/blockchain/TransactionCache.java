package app.server.blockchain;

import app.server.servers.jersey.model.AbstractTransaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ophir on 11/01/18.
 */
public class TransactionCache {

    private static final int threshold = 5;

    private List<AbstractTransaction> cache;

    public TransactionCache() {
        this.cache = new ArrayList<>();
    }

    public TransactionCache(List<AbstractTransaction> cache) {

        this.cache = cache;
    }

    public List<AbstractTransaction> getCache() {

        return cache;
    }

    public void setCache(List<AbstractTransaction> cache) {
        this.cache = cache;
    }

    public void addTransaction(AbstractTransaction transaction) {
        cache.add(transaction);
    }

    public List<AbstractTransaction> cacheOut() {
        ArrayList<AbstractTransaction> copy = new ArrayList<>(cache);
        cache.clear();
        return copy;
    }

    public boolean isFull() {
        return cache.size() >= threshold;
    }
}