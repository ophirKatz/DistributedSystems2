package servers.jersey.services;

import blockchain.Block;
import blockchain.BlockChain;
import servers.jersey.model.AbstractTransaction;
import servers.jersey.model.ShippingModel;

import java.util.List;

/**
 * Created by ophir on 08/01/18.
 */
public class ShippingService extends AbstractService<ShippingModel> {

    public ShippingService(BlockChain blockChain, List<AbstractTransaction> cache) {
        super(blockChain, cache);
    }

    @Override
    public void insertModelToBlockChain(ShippingModel model) {

    }

    public int getNumberOfShipmentsForShip(String shipId) {
        int numberOfShipments = 0;
        for (Block block : this.blockChain.getBlocks()) {
            for (AbstractTransaction transaction : block.getTransactions()) {
                //check if this is a shipping transaction
                if (transaction.getClass().equals(ShippingModel.class)) {
                    if (((ShippingModel) transaction).getShipID().equals(shipId)) {
                        if (((ShippingModel) transaction).getShipmentType()
                                == ShippingModel.ShipmentType.LEAVING) {
                            numberOfShipments++;
                        }
                    }
                }
            }
        }
        return numberOfShipments;
    }

    public int getNumberOfArrivalsForShip(String shipId) {
        int numberOfArrivals = 0;
        for (Block block : this.blockChain.getBlocks()) {
            for (AbstractTransaction transaction : block.getTransactions()) {
                //check if this is a shipping transaction
                if (transaction.getClass().equals(ShippingModel.class)) {
                    if (((ShippingModel) transaction).getShipID().equals(shipId)) {
                        if (((ShippingModel) transaction).getShipmentType()
                                == ShippingModel.ShipmentType.ARRIVING) {
                            numberOfArrivals++;
                        }
                    }
                }
            }
        }
        return numberOfArrivals;
    }
}
