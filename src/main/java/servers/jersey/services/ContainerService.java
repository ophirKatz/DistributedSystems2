package servers.jersey.services;

import blockchain.Block;
import blockchain.BlockChain;
import servers.jersey.model.AbstractTransaction;
import servers.jersey.model.ContainerModel;

import java.util.List;

/**
 * Created by ophir on 08/01/18.
 */
public class ContainerService extends AbstractService<ContainerModel> {

    public ContainerService(BlockChain blockChain, List<AbstractTransaction> cache) {
        super(blockChain, cache);
    }

    @Override
    public void insertModelToBlockChain(ContainerModel model) {
        // todo
        // 1. send model [transaction] to leader server.

        // 2. leader creates block [if cache is "full"] and inserts to blockchain.

        // 3. leader sends block to all other servers.

        // 4. servers receive block from leader and insert to their blockchain.
    }

    public String getShipIdForContainer(String containerId) {
        for (Block block : this.blockChain.getBlocks()) {
            for (AbstractTransaction transaction : block.getTransactions()) {
                //check if this is a container transaction
                if (transaction.getClass().equals(ContainerModel.class)) {
                    if (((ContainerModel) transaction).getContainerID().equals(containerId)) {
                        return ((ContainerModel) transaction).getShipID();
                    }
                }
            }
        }
        return null;
    }

    public int getNumberOfTransfersForContainer(String containerId) {
        int numberOfTransfers = 0;
        for (Block block : this.blockChain.getBlocks()) {
            for (AbstractTransaction transaction : block.getTransactions()) {
                //check if this is a container transaction
                if (transaction.getClass().equals(ContainerModel.class)) {
                    if (((ContainerModel) transaction).getContainerID().equals(containerId)) {
                        if (((ContainerModel) transaction).getContainmentType()
                                == ContainerModel.ContainmentType.LOADING) {
                            numberOfTransfers++;
                        }
                    }
                }
            }
        }
        return numberOfTransfers;
    }
}
