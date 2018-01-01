package transactions;

/**
 * Created by ophir on 01/01/18.
 */
public class StorageTransaction extends AbstractTransaction {
    String containerID;
    String itemID;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StorageTransaction that = (StorageTransaction) o;

        if (containerID != null ? !containerID.equals(that.containerID) : that.containerID != null) return false;
        return itemID != null ? itemID.equals(that.itemID) : that.itemID == null;
    }

    @Override
    public int hashCode() {
        int result = containerID != null ? containerID.hashCode() : 0;
        result = 31 * result + (itemID != null ? itemID.hashCode() : 0);
        return result;
    }

    public String getContainerID() {

        return containerID;
    }

    public void setContainerID(String containerID) {
        this.containerID = containerID;
    }

    public String getItemID() {

        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public StorageTransaction(String containerID, String itemID) {

        this.containerID = containerID;
        this.itemID = itemID;
    }
}
