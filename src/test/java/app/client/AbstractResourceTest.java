package app.client;

import app.Utils;
import org.junit.Before;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

/**
 * Created by ophir on 11/01/18.
 */
public class AbstractResourceTest {

    protected Client client;

    public static final int HTTP_CREATE = 201;

    protected static final String baseUri = "http://localhost:" + Utils.serverPort + "/shipchain";

    @Before
    public void setup() {
        client = ClientBuilder.newClient();
    }
}
