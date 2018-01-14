package app.client;

import app.server.servers.jersey.model.ContainerModel;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import static app.Utils.serverPort;

/**
 * Created by ophir on 11/01/18.
 */
@Ignore
public class ContainerResourceTest extends AbstractResourceTest {

    private static final String port = serverPort;

    @Test
    public void testLoadContainer() {
        ContainerModel newContainer = new ContainerModel("12345", "1234");
        String target = baseUri + "/containers/load";
        WebTarget loadTarget = client.target(target);
        Response response = loadTarget
                .request()
                .post(Entity.json(newContainer));
        Assert.assertEquals(HTTP_CREATE, response.getStatus());
    }

    @Test
    public void testContainerGetShipIdNotFound() {
        String target = baseUri + "/containers/getShipId?containerId=12345";
        String stringResponse = client.target(target)
                .request()
                .get(String.class);
        Assert.assertEquals("No container with id = 12345 was found", stringResponse);
    }

    @Test
    public void testContainerGetNumberOfTransfersNotFound() {
        String target = baseUri + "/containers/getNumberOfTransfers?containerId=12345";
        String stringResponse = client.target(target)
                .request()
                .get(String.class);
        Assert.assertEquals("Number of transfer of the container with id = 12345 is : 0", stringResponse);
    }
}
