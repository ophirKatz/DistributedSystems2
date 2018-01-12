package app.server.servers.jersey.resources;

import app.server.blockchain.BlockChain;
import app.server.blockchain.TransactionCache;
import app.server.servers.ServerProcess;
import app.server.servers.jersey.model.ContainerModel;
import app.server.servers.jersey.services.ContainerService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

import static app.server.servers.jersey.resources.ContainerResource.baseMapping;

/**
 * Created by ophir on 08/01/18.
 */
@Path(baseMapping)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.TEXT_PLAIN)
public class ContainerResource extends AbstractResource<ContainerService> {
    public static final String baseMapping = "/containers";

    private static final String cid = "containerId";
    private static final String sid = "shipId";

    @Inject
    public ContainerResource(BlockChain blockChain, TransactionCache cache, ServerProcess server) {
        this.service = new ContainerService(blockChain, cache, server);
        this.setReceiversByService();
    }

    private static ContainerModel createModel(String containerId, String shipId, ContainerModel.ContainmentType type) {
        ContainerModel model = new ContainerModel();
        model.setContainerID(containerId);
        model.setShipID(shipId);
        model.setContainmentType(type);
        return model;
    }

    @POST
    @Path("/load")
    public Response loadContainerOnShip(ContainerModel container) {
        ContainerModel model = createModel(container.getContainerID(), container.getShipID(), ContainerModel.ContainmentType.LOADING);
        try {
            service.attemptToExpandBlockChain(model);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        // TODO : check if just returning ok is fine or not
        System.out.println("Loaded container " + container.getContainerID() + " on ship " + container.getShipID());
        return Response.ok().build();
    }

    @POST
    @Path("/unload")
    public Response unloadContainerFromShip(ContainerModel container) {
        ContainerModel model = createModel(container.getContainerID(), container.getShipID(), ContainerModel.ContainmentType.UNLOADING);
        try {
            service.attemptToExpandBlockChain(model);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        System.out.println("Unloaded container " + container.getContainerID() + " from ship " + container.getShipID());
        return Response.ok().build();
    }

    @GET
    @Path("/getShipId")
    public String getShipId(@QueryParam(cid) String containerId) {
        Optional<String> shipIdForContainer = service.getShipIdForContainer(containerId);
        return shipIdForContainer.map(s -> "Ship id of container with id = " + containerId + " is : " + s)
                .orElseGet(() -> "No container with id = " + containerId + " was found");
    }

    @GET
    @Path("/getNumberOfTransfers")
    public String getNumberOfTransfers(@QueryParam(cid) String containerId) {
        return "Number of transfer of the container with id = " + containerId + " is : " + String.valueOf(service.getNumberOfTransfersForContainer(containerId));
    }
}











