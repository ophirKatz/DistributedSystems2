package servers.jersey.modeltransactions;

/**
 * Created by ophir on 08/01/18.
 */
public class ContainerTransaction {

    /**
     * Container c is loaded on ship s.
     * Container c is removed from ship s.
     */

    private String containerID;
    private String shipID;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ContainerTransaction that = (ContainerTransaction) o;

        if (containerID != null ? !containerID.equals(that.containerID) : that.containerID != null) return false;
        return shipID != null ? shipID.equals(that.shipID) : that.shipID == null;
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

    public ContainerTransaction(String containerID, String shipID) {

        this.containerID = containerID;
        this.shipID = shipID;
    }
}
