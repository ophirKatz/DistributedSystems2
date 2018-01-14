package app.server.servers.jersey.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by ophir on 13/01/18.
 */
@Path("/getit")
@Produces(MediaType.TEXT_PLAIN)
public class GetItResource {
    @GET
    public String getit() {
        return "Hello Ophir";
    }
}
