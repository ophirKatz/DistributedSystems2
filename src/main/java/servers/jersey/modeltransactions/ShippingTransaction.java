package servers.jersey.modeltransactions;

import transactions.AbstractTransaction;

/**
 * Created by ophir on 01/01/18.
 */
public class ShippingTransaction extends AbstractTransaction {

    /**
     * Ship s is leaving port p.
     * Ship s has arrived to port p.
     */
    private String shipID;
    private String portID;

    public ShippingTransaction(String shipID, String portID) {
        this.shipID = shipID;
        this.portID = portID;
    }

    public ShippingTransaction() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ShippingTransaction that = (ShippingTransaction) o;

        if (shipID != null ? !shipID.equals(that.shipID) : that.shipID != null) return false;
        return portID != null ? portID.equals(that.portID) : that.portID == null;
    }

    @Override
    public int hashCode() {
        int result = shipID != null ? shipID.hashCode() : 0;
        result = 31 * result + (portID != null ? portID.hashCode() : 0);
        return result;
    }

    public String getPortID() {

        return portID;
    }

    public void setPortID(String portID) {
        this.portID = portID;
    }

    public String getShipID() {

        return shipID;
    }

    public void setShipID(String shipID) {
        this.shipID = shipID;
    }
}
