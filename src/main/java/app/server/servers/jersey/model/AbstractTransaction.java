package app.server.servers.jersey.model;

import com.google.gson.Gson;

/**
 * Created by ophir on 01/01/18.
 */
public abstract class AbstractTransaction {
    private String id;

    public String getID() {
        return this.id;
    }

    /**
     * Serialize as JSON
     */
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    /**
     * Will have the following transactions:
     * Item i is stored on Container c.
     * Item i is removed from Container c.
     * Container c is loaded on ship s.
     * Container c is removed from ship s.
     * Ship s is leaving port p.
     * Ship s has arrived to port p.
     * */


}
