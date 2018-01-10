package app.server.servers.jersey.resources;

import app.server.blockchain.BlockChain;
import app.server.servers.ServerProcess;
import app.server.servers.jersey.model.AbstractTransaction;
import app.server.servers.jersey.model.ContainerModel;
import app.server.servers.jersey.services.ContainerService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.util.List;

import static app.server.servers.jersey.resources.ContainerResource.baseMapping;

/**
 * Created by ophir on 08/01/18.
 */
@Path(baseMapping)
public class ContainerResource extends AbstractResource<ContainerService> {
    public static final String baseMapping = "/containers";

    private static final String cid = "containerId";
    private static final String sid = "shipId";

    @Inject
    public ContainerResource(BlockChain blockChain, List<AbstractTransaction> cache, ServerProcess server) {
        this.service = new ContainerService(blockChain, cache, server);
    }

    private static ContainerModel createModel(String containerId, String shipId, ContainerModel.ContainmentType type) {
        ContainerModel model = new ContainerModel();
        model.setContainerID(containerId);
        model.setShipID(shipId);
        model.setContainmentType(type);
        return model;
    }

    @POST
    @Path("load")
    public Response loadContainerOnShip(@QueryParam(cid) String containerId,
                                        @QueryParam(sid) String shipId) {
        ContainerModel model = createModel(containerId, shipId, ContainerModel.ContainmentType.LOADING);
        try {
            service.attemptToExpandBlockChain(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // TODO : check if just returning ok is fine or not
        return Response.ok().build();
    }

    @POST
    @Path("unload")
    public Response unloadContainerFromShip(@QueryParam(cid) String containerId,
                                            @QueryParam(sid) String shipId) {
        ContainerModel model = createModel(containerId, shipId, ContainerModel.ContainmentType.UNLOADING);
        try {
            service.attemptToExpandBlockChain(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.ok().build();
    }

    @GET
    @Path("getShipId")
    public String getShipId(@QueryParam(cid) String containerId) {
        return service.getShipIdForContainer(containerId);
    }

    @GET
    @Path("getNumberOfTransfers")
    public String getNumberOfTransfers(@QueryParam(cid) String containerId) {
        return String.valueOf(service.getNumberOfTransfersForContainer(containerId));
    }
}











