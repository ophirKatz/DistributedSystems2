package servers.jersey.resources;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import static servers.jersey.resources.ShippingResource.baseMapping;

/**
 * Created by ophir on 08/01/18.
 */

@Path(baseMapping)
public class ShippingResource {
    public static final String baseMapping = "/shipping";

    public static final String sid = "shipId";
    public static final String pid = "portId";

    @POST
    @Path("shipLeaving")
    public Response shipLeaving(@QueryParam(sid) String containerId,
                                @QueryParam(pid) String portId) {
        return null;
    }

    @POST
    @Path("shipArriving")
    public Response shipArriving(@QueryParam(sid) String containerId,
                                 @QueryParam(pid) String portId) {
        return null;
    }

    @GET
    @Path("numberOfShipments")
    public String getNumberOfShipments(@QueryParam(sid) String containerId) {
        return null;
    }

    @GET
    @Path("numberOfPortingShips")
    public String getNumberOfPortingShips(@QueryParam(pid) String portId) {
        return null;
    }
}
