package servers.jersey.services;

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
        return 0;
    }

    public int getNumberOfArrivalsForShip(String shipId) {
        return 0;
    }
}
