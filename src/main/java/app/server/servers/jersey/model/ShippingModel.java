package app.server.servers.jersey.model;

import com.google.gson.Gson;

/**
 * Created by ophir on 01/01/18.
 */
public class ShippingModel extends AbstractTransaction {

    /**
     * Ship s is leaving port p.
     * Ship s has arrived to port p.
     */

    public enum ShipmentType {
        LEAVING,
        ARRIVING
    }

    private String shipID;
    private String portID;
    private ShipmentType shipmentType;

    public ShipmentType getShipmentType() {
        return shipmentType;
    }

    public void setShipmentType(ShipmentType shipmentType) {
        this.shipmentType = shipmentType;
    }

    public ShippingModel() {
    }

    public ShippingModel(String shipID, String portID) {
        this.shipID = shipID;
        this.portID = portID;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ShippingModel that = (ShippingModel) o;

        return (shipID != null ? shipID.equals(that.shipID) : that.shipID == null) &&
                (portID != null ? portID.equals(that.portID) : that.portID == null);
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

    public static ShippingModel parseString(String s) {
        Gson gson = new Gson();
        return gson.fromJson(s, ShippingModel.class);
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
