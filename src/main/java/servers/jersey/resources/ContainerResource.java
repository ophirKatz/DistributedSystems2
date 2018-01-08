package servers.jersey.resources;

import servers.jersey.services.ContainerService;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import static servers.jersey.resources.ContainerResource.baseMapping;

/**
 * Created by ophir on 08/01/18.
 */
@Path(baseMapping)
public class ContainerResource extends AbstractResource<ContainerService> {
    public static final String baseMapping = "/containers";

    private static final String cid = "containerId";
    private static final String sid = "shipId";

    @POST
    @Path("load")
    public Response loadContainerOnShip(@QueryParam(cid) String containerId,
                                        @QueryParam(sid) String shipId) {
        return null;
    }

    @POST
    @Path("unload")
    public Response unloadContainerFromShip(@QueryParam(cid) String containerId,
                                            @QueryParam(sid) String shipId) {
        return null;
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











