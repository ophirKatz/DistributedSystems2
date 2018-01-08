package servers.jersey.modeltransactions;

import transactions.AbstractTransaction;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by ophir on 01/01/18.
 */
@XmlRootElement
public class ManufactureTransaction extends AbstractTransaction {
    private String itemID;

    public ManufactureTransaction() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ManufactureTransaction that = (ManufactureTransaction) o;

        return itemID != null ? itemID.equals(that.itemID) : that.itemID == null;
    }

    @Override
    public int hashCode() {
        return itemID != null ? itemID.hashCode() : 0;
    }

    public String getItemID() {

        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public ManufactureTransaction(String itemID) {

        this.itemID = itemID;
    }
}
