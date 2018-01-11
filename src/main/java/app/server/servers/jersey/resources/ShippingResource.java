package app.server.servers.jersey.resources;

import app.server.blockchain.BlockChain;
import app.server.servers.ServerProcess;
import app.server.servers.jersey.model.AbstractTransaction;
import app.server.servers.jersey.model.ShippingModel;
import app.server.servers.jersey.services.ShippingService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static app.server.servers.jersey.resources.ShippingResource.baseMapping;

/**
 * Created by ophir on 08/01/18.
 */

@Path(baseMapping)
@Consumes(MediaType.APPLICATION_JSON)
public class ShippingResource extends AbstractResource<ShippingService> {
    public static final String baseMapping = "/shipping";

    public static final String sid = "shipId";
    public static final String pid = "portId";

    @Inject
    public ShippingResource(BlockChain blockChain, List<AbstractTransaction> cache, ServerProcess server) {
        this.service = new ShippingService(blockChain, cache, server);
    }

    private static ShippingModel createModel(String shipId, String portId, ShippingModel.ShipmentType type) {
        ShippingModel model = new ShippingModel();
        model.setShipID(shipId);
        model.setPortID(portId);
        model.setShipmentType(type);
        return model;
    }

    @POST
    @Path("/shipLeaving")
    public Response shipLeaving(ShippingModel shippingModel) {
        ShippingModel model = createModel(shippingModel.getShipID(), shippingModel.getPortID(), ShippingModel.ShipmentType.LEAVING);
        try {
            service.attemptToExpandBlockChain(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.ok().build();
    }

    @POST
    @Path("/shipArriving")
    public Response shipArriving(ShippingModel shippingModel) {
        ShippingModel model = createModel(shippingModel.getShipID(), shippingModel.getPortID(), ShippingModel.ShipmentType.ARRIVING);
        try {
            service.attemptToExpandBlockChain(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.ok().build();
    }

    @GET
    @Path("/numberOfShipments")
    public String getNumberOfShipments(@QueryParam(sid) String shipId) {
        return String.valueOf(service.getNumberOfShipmentsForShip(shipId));
    }

    @GET
    @Path("/numberOfArrivals")
    public String getNumberOfArrivals(@QueryParam(sid) String shipId) {
        return String.valueOf(service.getNumberOfArrivalsForShip(shipId));
    }
}
