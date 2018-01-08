package datatranslation;

import servers.jersey.modeltransactions.ShippingTransaction;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by ophir on 07/01/18.
 */
@Path("/transactions")
public class TransactionResource {

    @GET
    @Path("/getShippingTransaction")
    @Produces(MediaType.APPLICATION_JSON)
    public ShippingTransaction getShippingTransaction() {
        ShippingTransaction shippingTransaction = new ShippingTransaction();
        shippingTransaction.setItemID("I12");
        shippingTransaction.setShipID("S13");
        return shippingTransaction;
    }
}
