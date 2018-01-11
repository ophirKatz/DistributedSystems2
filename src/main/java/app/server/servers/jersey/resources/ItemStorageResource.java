package app.server.servers.jersey.resources;

import app.server.blockchain.BlockChain;
import app.server.blockchain.TransactionCache;
import app.server.servers.ServerProcess;
import app.server.servers.jersey.model.StorageModel;
import app.server.servers.jersey.services.StorageService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static app.server.servers.jersey.resources.ItemStorageResource.baseMapping;

/**
 * Created by ophir on 08/01/18.
 */
@Path(baseMapping)
@Consumes(MediaType.APPLICATION_JSON)
public class ItemStorageResource extends AbstractResource<StorageService> {
    public static final String baseMapping = "/storage";

    public static final String cid = "containerId";
    public static final String iid = "itemId";

    @Inject
    public ItemStorageResource(BlockChain blockChain, TransactionCache cache, ServerProcess server) {
        this.service = new StorageService(blockChain, cache, server);
    }

    private static StorageModel createModel(String containerId, String itemId, StorageModel.StorageType type) {
        StorageModel model = new StorageModel();
        model.setContainerID(containerId);
        model.setItemID(itemId);
        model.setStorageType(type);
        return model;
    }

    @POST
    @Path("/store")
    public Response insertItemToContainer(StorageModel storageModel) {
        StorageModel model = createModel(storageModel.getContainerID(), storageModel.getItemID(), StorageModel.StorageType.STORE);
        try {
            service.attemptToExpandBlockChain(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.ok().build();
    }

    @POST
    @Path("/remove")
    public Response removeItemFromContainer(StorageModel storageModel) {
        StorageModel model = createModel(storageModel.getContainerID(), storageModel.getItemID(), StorageModel.StorageType.REMOVE);
        try {
            service.attemptToExpandBlockChain(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.ok().build();
    }

    @GET
    @Path("/getContainerId")
    public String getContainerId(@QueryParam(iid) String itemId) {
        return "Container id of item with id = " + itemId + " is : " + service.getIdOfItemContainer(itemId);
    }

    @GET
    @Path("/numberOfItems")
    public String getNumberOfItems(@QueryParam(cid) String containerId) {
        return "Number of items in container with id = " + containerId + " is : " + String.valueOf(service.getNumberOfItemsInContainer(containerId));
    }
}
