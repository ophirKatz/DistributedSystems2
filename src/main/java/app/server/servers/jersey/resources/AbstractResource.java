package app.server.servers.jersey.resources;

import app.server.servers.jersey.services.AbstractService;

/**
 * Created by ophir on 08/01/18.
 */
public class AbstractResource<ServiceType extends AbstractService> {

    protected static final String applicationPath = "/shipchain";

    protected ServiceType service;

    protected void setReceiversByService() {
        service.setReceiversForServerProcess();
    }
}
