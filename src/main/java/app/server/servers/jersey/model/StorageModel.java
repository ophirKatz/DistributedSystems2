package app.server.servers.jersey.model;

import com.google.gson.Gson;

/**
 * Created by ophir on 01/01/18.
 */
public class StorageModel extends AbstractTransaction {

    /**
     * Item i is stored on Container c.
     * Item i is removed from Container c.
     */

    public enum StorageType {
        STORE,
        REMOVE
    }

    private String containerID;
    private String itemID;

    public StorageModel(String containerID, String itemID) {
        this.containerID = containerID;
        this.itemID = itemID;
    }

    public StorageType getStorageType() {
        return storageType;
    }

    public void setStorageType(StorageType storageType) {
        this.storageType = storageType;
    }

    private StorageType storageType;

    public StorageModel() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StorageModel that = (StorageModel) o;

        return (containerID != null ? containerID.equals(that.containerID) : that.containerID == null) &&
                (itemID != null ? itemID.equals(that.itemID) : that.itemID == null);
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

    public static StorageModel parseString(String s) {
        Gson gson = new Gson();
        return gson.fromJson(s, StorageModel.class);
    }

}
