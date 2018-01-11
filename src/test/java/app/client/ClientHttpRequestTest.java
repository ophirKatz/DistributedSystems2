package app.client;

import app.server.servers.jersey.model.ContainerModel;
import org.junit.Assert;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import static app.Utils.serverPort;

/**
 * Created by ophir on 11/01/18.
 */
public class ClientHttpRequestTest {

    private static final String port = serverPort;

    //@Test
    public void testContainerLoad() {
        ContainerModel newContainer = new ContainerModel("containerIr", "shipId");
        Client client = ClientBuilder.newClient();
        String applicationPath = "/shipchain";
        final String path = "/load";
        System.out.println("Request to path : " + "http://localhost:" + port + applicationPath + path);
        WebTarget containerTarget = client.target("http://localhost:" + port + applicationPath)
                .path(path);
        Response postResponse = containerTarget
                .request()
                .post(Entity.json(newContainer));
        if (postResponse.getStatus() != 201) {
            System.out.println("Error Code : " + postResponse.getStatus());
        }
        String stringResponse = postResponse.readEntity(String.class);
        System.out.println("String response from resource = " + stringResponse);
    }

    @Test
    public void testContainerGetShipId() {
        Client client = ClientBuilder.newClient();
        String target = "http://localhost:8282/shipchain/containers/getShipId?containerId=12345";
        String stringResponse = client.target(target)
                .request()
                .get(String.class);
        Assert.assertEquals("Ship id of container with id = 12345 is : shipId", stringResponse);
    }
}
