package servers.jersey.services;

import blockchain.Block;
import blockchain.BlockChain;
import servers.jersey.model.AbstractTransaction;
import servers.jersey.model.StorageModel;

import java.util.List;

/**
 * Created by ophir on 08/01/18.
 */
public class StorageService extends AbstractService<StorageModel> {

    public StorageService(BlockChain blockChain, List<AbstractTransaction> cache) {
        super(blockChain, cache);
    }

    @Override
    public void insertModelToBlockChain(StorageModel model) {

    }

    public String getIdOfItemContainer(String itemId) {
        String containerId = null;
        for (Block block : this.blockChain.getBlocks()) {
            for (AbstractTransaction transaction : block.getTransactions()) {
                //check if this is a storage transaction
                if (transaction.getClass().equals(StorageModel.class)) {
                    if (((StorageModel) transaction).getItemID().equals(itemId)) {
                        containerId = ((StorageModel) transaction).getContainerID();
                    }
                }
            }
        }
        return containerId;
    }

    public int getNumberOfItemsInContainer(String containerId) {
        int numOfItems = 0;
        for (Block block : this.blockChain.getBlocks()) {
            for (AbstractTransaction transaction : block.getTransactions()) {
                //check if this is a storage transaction
                if (transaction.getClass().equals(StorageModel.class)) {
                    if (((StorageModel) transaction).getContainerID().equals(containerId)) {
                        if (((StorageModel) transaction).getStorageType().equals(StorageModel.StorageType.STORE)) {
                            numOfItems++;
                        } else {
                            numOfItems--;
                        }
                    }
                }
            }
        }
        return numOfItems;
    }
}
