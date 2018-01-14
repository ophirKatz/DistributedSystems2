package app.client;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Created by ophir on 11/01/18.
 */
@Ignore
public class ShippingResourceTest extends AbstractResourceTest {

    @Test
    public void testShippingGetNumberOfShipmentsNotFound() {
        String target = baseUri + "/shipping/numberOfShipments?shipId=123456";
        String stringResponse = client.target(target)
                .request()
                .get(String.class);
        Assert.assertEquals("Number of times ship with id = 123456 has left a port is : 0", stringResponse);
    }

    @Test
    public void testShippingGetNumberOfArrivalsNotFound() {
        String target = baseUri + "/shipping/numberOfArrivals?shipId=123456";
        String stringResponse = client.target(target)
                .request()
                .get(String.class);
        Assert.assertEquals("Number of times ship with id = 123456 has arrived a port is : 0", stringResponse);
    }

}
