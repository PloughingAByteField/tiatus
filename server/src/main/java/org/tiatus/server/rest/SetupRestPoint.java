package org.tiatus.server.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.entity.User;
import org.tiatus.role.Role;
import org.tiatus.service.ServiceException;
import org.tiatus.service.UserService;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;

/**
 * Created by johnreynolds on 04/09/2016.
 */
@Path("setup")
@SuppressWarnings("squid:S1166")
public class SetupRestPoint {

    private static final Logger LOG = LoggerFactory.getLogger(SetupRestPoint.class);

    private UserService service;

    /**
     * Add an admin user via setup page
     * @param uriInfo location details
     * @param user user to add
     * @return response contain success or failure code
     */
    @PermitAll
    @POST
    @Path("user")
    @Consumes("application/json")
    @Produces("application/json")
    public Response addUser(@Context UriInfo uriInfo, User user) {
        LOG.debug("Adding user " + user);
        try {
            service.addUser(user);
            return Response.created(URI.create(uriInfo.getPath() + "/"+ user.getId())).entity(user).build();

        } catch (ServiceException e) {
            LOG.warn("Got service exception: ", e.getSuppliedException());
            throw new InternalServerErrorException();

        } catch (Exception e) {
            LOG.warn("Got general exception ", e);
            throw new InternalServerErrorException();
        }
    }

    /**
     * Add customization
     * @return response contain success or failure code
     */
    @RolesAllowed({Role.ADMIN})
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response addConfig() {
        File file = new File("/www/tiatus/config/css/custom.css");
        try (FileOutputStream fop = new FileOutputStream(file)){
            fop.write("hello".getBytes());
        } catch (IOException e) {
            LOG.warn("Got general exception ", e);
            throw new InternalServerErrorException();
        }
        return Response.ok().build();
    }

    @Inject
    // sonar want constructor injection which jaxrs does not support
    public void setService(UserService service) {
        this.service = service;
    }
}
