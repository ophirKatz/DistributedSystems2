package app.servers.jersey.services;

import app.blockchain.BlockChain;
import app.servers.jersey.ContextBindingModule;
import app.servers.jersey.model.AbstractTransaction;
import app.servers.jersey.model.StorageModel;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ophir on 08/01/18.
 */
public class StorageService extends AbstractService<StorageModel> {

    public StorageService(BlockChain blockChain, @ContextBindingModule.TransactionCache List<AbstractTransaction> cache) {
        super(blockChain, cache);
    }

    @Override
    public void insertModelToBlockChain(StorageModel model) {

    }

    /**
     * return the id of the container that contains the item with the given id.
     */
    public String getIdOfItemContainer(String itemId) {
        String containerId = null;
        for (AbstractTransaction transaction : getAllTransactionsInBlockChainByModelClass(StorageModel.class)
                .stream().filter(t -> ((StorageModel) t).getItemID().equals(itemId))
                .collect(Collectors.toList())) {
            containerId = ((StorageModel) transaction).getContainerID();
        }
        return containerId;
    }

    /**
     * return the number of items in the container with the given id.
     */
    public int getNumberOfItemsInContainer(String containerId) {
        int numOfItems = 0;
        for (AbstractTransaction transaction : getAllTransactionsInBlockChainByModelClass(StorageModel.class)
                .stream().filter(t -> ((StorageModel) t).getContainerID().equals(containerId))
                .collect(Collectors.toList())) {
            if (((StorageModel) transaction).getStorageType().equals(StorageModel.StorageType.STORE)) {
                numOfItems++;
            } else {
                numOfItems--;
            }
        }
        return numOfItems;
    }
}
