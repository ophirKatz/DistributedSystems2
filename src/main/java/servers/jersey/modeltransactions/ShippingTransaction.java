package servers.jersey.modeltransactions;

import transactions.AbstractTransaction;

/**
 * Created by ophir on 01/01/18.
 */
public class ShippingTransaction extends AbstractTransaction {
    private String shipID;
    private String itemID;

    public ShippingTransaction(String shipID, String itemID) {
        this.shipID = shipID;
        this.itemID = itemID;
    }

    public ShippingTransaction() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ShippingTransaction that = (ShippingTransaction) o;

        if (shipID != null ? !shipID.equals(that.shipID) : that.shipID != null) return false;
        return itemID != null ? itemID.equals(that.itemID) : that.itemID == null;
    }

    @Override
    public int hashCode() {
        int result = shipID != null ? shipID.hashCode() : 0;
        result = 31 * result + (itemID != null ? itemID.hashCode() : 0);
        return result;
    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public String getShipID() {

        return shipID;
    }

    public void setShipID(String shipID) {
        this.shipID = shipID;
    }
}
