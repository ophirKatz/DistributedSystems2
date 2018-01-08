package servers.jersey.resources;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import static servers.jersey.resources.ItemStorageResource.baseMapping;

/**
 * Created by ophir on 08/01/18.
 */
@Path(baseMapping)
public class ItemStorageResource {
    public static final String baseMapping = "/storage";

    public static final String cid = "containerId";
    public static final String iid = "itemId";

    @POST
    @Path("insert")
    public Response insertItemToContainer(@QueryParam(cid) String containerId,
                                          @QueryParam(iid) String itemId) {
        return null;
    }

    @POST
    @Path("remove")
    public Response removeItemFromContainer(@QueryParam(cid) String containerId,
                                            @QueryParam(iid) String itemId) {
        return null;
    }

    @GET
    @Path("getContainerId")
    public String getContainerId(@QueryParam(iid) String itemId) {
        return null;
    }

    @GET
    @Path("numberOfItems")
    public String getNumberOfItems(@QueryParam(cid) String containerId) {
        return null;
    }
}
