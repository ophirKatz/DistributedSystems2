package app.server.servers.jersey.model;

import com.google.gson.Gson;

/**
 * Created by ophir on 08/01/18.
 */
public class ContainerModel extends AbstractTransaction {
    /**
     * Container c is loaded on ship s.
     * Container c is removed from ship s.
     */

    public enum ContainmentType {
        LOADING,
        UNLOADING;
    }
    private String containerID;

    private String shipID;
    private ContainmentType containmentType;
    public ContainmentType getContainmentType() {
        return containmentType;
    }

    public void setContainmentType(ContainmentType containmentType) {
        this.containmentType = containmentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ContainerModel that = (ContainerModel) o;

        return (containerID != null ? containerID.equals(that.containerID) : that.containerID == null) &&
                (shipID != null ? shipID.equals(that.shipID) : that.shipID == null);
    }

    @Override
    public int hashCode() {
        int result = containerID != null ? containerID.hashCode() : 0;
        result = 31 * result + (shipID != null ? shipID.hashCode() : 0);
        return result;
    }

    public String getContainerID() {

        return containerID;
    }

    public void setContainerID(String containerID) {
        this.containerID = containerID;
    }

    public String getShipID() {
        return shipID;
    }

    public void setShipID(String shipID) {
        this.shipID = shipID;
    }

    public static ContainerModel parseString(String s) {
        Gson gson = new Gson();
        return gson.fromJson(s, ContainerModel.class);
    }
}
