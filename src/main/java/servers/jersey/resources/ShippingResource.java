package servers.jersey.resources;

import blockchain.BlockChain;
import servers.jersey.model.AbstractTransaction;
import servers.jersey.model.ShippingModel;
import servers.jersey.services.ShippingService;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.util.List;

import static servers.jersey.resources.ShippingResource.baseMapping;

/**
 * Created by ophir on 08/01/18.
 */

@Path(baseMapping)
public class ShippingResource extends AbstractResource<ShippingService> {
    public static final String baseMapping = "/shipping";

    public static final String sid = "shipId";
    public static final String pid = "portId";

    public ShippingResource(BlockChain blockChain, List<AbstractTransaction> cache) {
        this.service = new ShippingService(blockChain, cache);
    }

    private static ShippingModel createModel(String shipId, String portId, ShippingModel.ShipmentType type) {
        ShippingModel model = new ShippingModel();
        model.setShipID(shipId);
        model.setPortID(portId);
        model.setShipmentType(type);
        return model;
    }

    @POST
    @Path("shipLeaving")
    public Response shipLeaving(@QueryParam(sid) String shipId,
                                @QueryParam(pid) String portId) {
        ShippingModel model = createModel(shipId, portId, ShippingModel.ShipmentType.LEAVING);
        service.insertModelToBlockChain(model);
        return Response.ok().build();
    }

    @POST
    @Path("shipArriving")
    public Response shipArriving(@QueryParam(sid) String shipId,
                                 @QueryParam(pid) String portId) {
        ShippingModel model = createModel(shipId, portId, ShippingModel.ShipmentType.ARRIVING);
        service.insertModelToBlockChain(model);
        return Response.ok().build();
    }

    @GET
    @Path("numberOfShipments")
    public String getNumberOfShipments(@QueryParam(sid) String shipId) {
        return null;
    }

    @GET
    @Path("numberOfArrivals")
    public String getNumberOfArrivals(@QueryParam(sid) String shipId) {
        return null;
    }
}
