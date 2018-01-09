package app.servers.jersey.services;

import app.blockchain.BlockChain;
import app.servers.jersey.ContextBindingModule;
import app.servers.jersey.model.AbstractTransaction;
import app.servers.jersey.model.ShippingModel;

import java.util.List;

/**
 * Created by ophir on 08/01/18.
 */
public class ShippingService extends AbstractService<ShippingModel> {

    public ShippingService(BlockChain blockChain, @ContextBindingModule.TransactionCache List<AbstractTransaction> cache) {
        super(blockChain, cache);
    }

    @Override
    public void insertModelToBlockChain(ShippingModel model) {

    }

    private int getNumberOfShipActions(String shipId, ShippingModel.ShipmentType shipmentType) {
        return (int) getAllTransactionsInBlockChainByModelClass(ShippingModel.class).stream()
                .filter(t -> {
                    ShippingModel model = (ShippingModel) t;
                    return model.getShipID().equals(shipId) &&
                            model.getShipmentType().equals(shipmentType);
                }).count();
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
