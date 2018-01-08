package servers.jersey.resources;

import blockchain.BlockChain;
import servers.jersey.model.AbstractTransaction;
import servers.jersey.model.ContainerModel;
import servers.jersey.services.ContainerService;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.util.List;

import static servers.jersey.resources.ContainerResource.baseMapping;

/**
 * Created by ophir on 08/01/18.
 */
@Path(baseMapping)
public class ContainerResource extends AbstractResource<ContainerService> {
    public static final String baseMapping = "/containers";

    private static final String cid = "containerId";
    private static final String sid = "shipId";

    public ContainerResource(BlockChain blockChain, List<AbstractTransaction> cache) {
        this.service = new ContainerService(blockChain, cache);
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
        service.insertModelToBlockChain(model);
        // TODO : check if just returning ok is fine or not
        return Response.ok().build();
    }

    @POST
    @Path("unload")
    public Response unloadContainerFromShip(@QueryParam(cid) String containerId,
                                            @QueryParam(sid) String shipId) {
        ContainerModel model = createModel(containerId, shipId, ContainerModel.ContainmentType.UNLOADING);
        service.insertModelToBlockChain(model);
        return Response.ok().build();
    }

    @GET
    @Path("getShipId")
    public String getShipId(@QueryParam(cid) String containerId) {
        return null;
    }

    @GET
    @Path("getNumberOfTransfers")
    public String getNumberOfTransfers(@QueryParam(cid) String containerId) {
        return null;
    }
}











