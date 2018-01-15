package app.server.servers.jersey.services;

import app.server.blockchain.BlockChain;
import app.server.blockchain.TransactionCache;
import app.server.servers.ServerProcess;
import app.server.servers.jersey.model.AbstractTransaction;
import app.server.servers.jersey.model.ContainerModel;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by ophir on 08/01/18.
 */
public class ContainerService extends AbstractService<ContainerModel> {

    public ContainerService(BlockChain blockChain, TransactionCache cache, ServerProcess server) {
        super(blockChain, cache, server, ContainerModel.class);
    }

    @Override
    public void attemptToExpandBlockChain(ContainerModel model) throws Exception {
        super.attemptToExpandBlockChain(model);
    }

    /**
     * returns the id of the ship that the container with the id is loaded on.
     */
    public Optional<String> getShipIdForContainer(String containerId) {
        Optional<String> shipId = Optional.empty();
        for (AbstractTransaction transaction : getAllTransactionsInBlockChainByModelClass(ContainerModel.class)
                .stream().filter(t -> ((ContainerModel) t).getContainerID().equals(containerId))
                .collect(Collectors.toList())) {
            shipId = Optional.of(((ContainerModel) transaction).getShipID());
        }
        return shipId;
    }

    /**
     * returns the number of transfers a container has been part of. This translates to the
     * number of times it was loaded on a ship.
     */
    public int getNumberOfTransfersForContainer(String containerId) {
        List<AbstractTransaction> allTransactionsInBlockChainByModelClass = getAllTransactionsInBlockChainByModelClass(ContainerModel.class);
        long count = allTransactionsInBlockChainByModelClass
                .stream().filter(t -> {
                    ContainerModel model = (ContainerModel) t;
                    return model.getContainerID().equals(containerId) &&
                            model.getContainmentType().equals(ContainerModel.ContainmentType.LOADING);
                }).count();
        return (int) count;
    }
}
