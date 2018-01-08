package servers.jersey.model;

/**
 * Created by ophir on 01/01/18.
 */
public abstract class AbstractTransaction {
    private String id;

    public String getID() {
        return this.id;
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
