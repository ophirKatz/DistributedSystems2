package servers.jersey.services;

import blockchain.BlockChain;
import servers.jersey.model.AbstractTransaction;
import servers.jersey.model.ContainerModel;

import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * returns the id of the ship that the container with the id is loaded on.
     */
    public String getShipIdForContainer(String containerId) {
        String shipId = null;
        for (AbstractTransaction transaction : getAllTransactionsInBlockChainByModelClass(ContainerModel.class)
                .stream().filter(t -> ((ContainerModel) t).getContainerID().equals(containerId))
                .collect(Collectors.toList())) {
            shipId = ((ContainerModel) transaction).getShipID();

        }
        return shipId;
    }

    /**
     * returns the number of transfers a container has been part of. This translates to the
     * number of times it was loaded on a ship.
     */
    public int getNumberOfTransfersForContainer(String containerId) {
        return (int) getAllTransactionsInBlockChainByModelClass(ContainerModel.class)
                .stream().filter(t -> {
                    ContainerModel model = (ContainerModel) t;
                    return model.getContainerID().equals(containerId) &&
                            model.getContainmentType().equals(ContainerModel.ContainmentType.LOADING);
                }).count();
    }
}
