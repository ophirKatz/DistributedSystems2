package app.servers.jersey.resources;

import app.servers.jersey.services.AbstractService;

/**
 * Created by ophir on 08/01/18.
 */
public class AbstractResource<ServiceType extends AbstractService> {

    protected ServiceType service;
}
