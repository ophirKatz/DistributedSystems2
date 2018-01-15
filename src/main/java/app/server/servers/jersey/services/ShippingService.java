package app.server.servers.jersey.services;

import app.server.blockchain.BlockChain;
import app.server.blockchain.TransactionCache;
import app.server.servers.ServerProcess;
import app.server.servers.jersey.model.AbstractTransaction;
import app.server.servers.jersey.model.ShippingModel;

import java.util.List;

/**
 * Created by ophir on 08/01/18.
 */
public class ShippingService extends AbstractService<ShippingModel> {

    public ShippingService(BlockChain blockChain, TransactionCache cache, ServerProcess server) {
        super(blockChain, cache, server, ShippingModel.class);
    }

    @Override
    public void attemptToExpandBlockChain(ShippingModel model) throws Exception {
        super.attemptToExpandBlockChain(model);
    }

    private int getNumberOfShipActions(String shipId, ShippingModel.ShipmentType shipmentType) {
        List<AbstractTransaction> allTransactionsInBlockChainByModelClass = getAllTransactionsInBlockChainByModelClass(ShippingModel.class);
        long count = allTransactionsInBlockChainByModelClass.stream()
                .filter(t -> {
                    ShippingModel model = (ShippingModel) t;
                    return model.getShipID().equals(shipId) &&
                            model.getShipmentType().equals(shipmentType);
                }).count();
        return (int) count;
    }

    /**
     * return the number of shipments the ship with the given id has been part of.
     * This is the number of times the ship left a port.
     */
    public int getNumberOfShipmentsForShip(String shipId) {
        return getNumberOfShipActions(shipId, ShippingModel.ShipmentType.LEAVING);
    }

    /**
     * return the number of shipments arrivals the ship with the given id has been part of.
     * This is the number of times the ship arrived to a port.
     */
    public int getNumberOfArrivalsForShip(String shipId) {
        return getNumberOfShipActions(shipId, ShippingModel.ShipmentType.ARRIVING);
    }
}
