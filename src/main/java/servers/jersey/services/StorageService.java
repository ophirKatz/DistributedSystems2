package servers.jersey.services;

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
        return null;
    }

    public int getNumberOfItemsInContainer(String containerId) {
        return 0;
    }
}
