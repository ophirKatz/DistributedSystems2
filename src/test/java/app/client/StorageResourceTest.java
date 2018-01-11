package app.client;

import org.junit.Assert;

/**
 * Created by ophir on 11/01/18.
 */
public class StorageResourceTest extends AbstractResourceTest {

    //@Test
    public void testStorageGetContainerIdNotFound() {
        String target = baseUri + "/storage/getContainerId?itemId=12345";
        String stringResponse = client.target(target)
                .request()
                .get(String.class);
        Assert.assertEquals("No item with id = 12345 was found", stringResponse);
    }

    //@Test
    public void testStorageGetNumberOfItemsNotFound() {
        String target = baseUri + "/storage/numberOfItems?containerId=12345";
        String stringResponse = client.target(target)
                .request()
                .get(String.class);
        Assert.assertEquals("Number of items in container with id = 12345 is : 0", stringResponse);
    }
}
