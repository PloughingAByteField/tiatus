package org.tiatus.server.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.entity.User;
import org.tiatus.role.Role;
import org.tiatus.service.ServiceException;
import org.tiatus.service.UserService;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

/**
 * Created by johnreynolds on 27/02/2017.
 */
@Path("users")
@SuppressWarnings("squid:S1166")
public class UserRestPoint {
    private static final Logger LOG = LoggerFactory.getLogger(UserRestPoint.class);

    private UserService service;

    /**
     * Get users
     * @return response containing list of users
     */
    @RolesAllowed({Role.ADMIN})
    @GET
    @Produces("application/json")
    public Response getUsers() {
        try {
            List<User> users = service.getUsers();
            return Response.ok(users).build();

        } catch (ServiceException e) {
            LOG.warn("Got service exception: ", e.getSuppliedException());
            throw new InternalServerErrorException();

        } catch (Exception e) {
            LOG.warn("Got general exception ", e);
            throw new InternalServerErrorException();
        }
    }

    @RolesAllowed({Role.ADMIN})
    @GET
    @Path("roles")
    @Produces("application/json")
    public Response getUserRoles() {
        try {
            List<org.tiatus.entity.Role> userRoles = service.getUserRoles();
            return Response.ok(userRoles).build();

        } catch (ServiceException e) {
            LOG.warn("Got service exception: ", e.getSuppliedException());
            throw new InternalServerErrorException();

        } catch (Exception e) {
            LOG.warn("Got general exception ", e);
            throw new InternalServerErrorException();
        }
    }

    /**
     * Add user, restricted to Admin users
     * @param uriInfo location details
     * @param user to add
     * @return 201 response with location containing uri of newly created user or an error code
     */
    @RolesAllowed({Role.ADMIN})
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response addUser(@Context UriInfo uriInfo, User user) {
        LOG.debug("Adding user " + user);
        try {
            User saved = service.addUser(user);
            return Response.created(URI.create(uriInfo.getPath() + "/"+ saved.getId())).entity(saved).build();

        } catch (ServiceException e) {
            LOG.warn("Got service exception: ", e.getSuppliedException());
            throw new InternalServerErrorException();

        } catch (Exception e) {
            LOG.warn("Got general exception ", e);
            throw new InternalServerErrorException();
        }
    }

    /**
     * Remove user, restricted to Admin users
     * @param id of user to remove
     * @return response with 204
     */
    @RolesAllowed({Role.ADMIN})
    @DELETE
    @Path("{id}")
    @Produces("application/json")
    public Response removeUser(@PathParam("id") String id) {
        LOG.debug("Removing user with id " + id);
        try {
            User user = new User();
            user.setId(Long.parseLong(id));
            service.deleteUser(user);
            return Response.noContent().build();

        } catch (ServiceException e) {
            LOG.warn("Got service exception: ", e.getSuppliedException());
            throw new InternalServerErrorException();

        } catch (Exception e) {
            LOG.warn("Got general exception ", e);
            throw new InternalServerErrorException();
        }
    }

    /**
     * Update user, restricted to Admin users
     * @param user to update
     * @return 204 response or an error code
     */
    @PUT
    @RolesAllowed({Role.ADMIN})
    @Produces("application/json")
    public Response updateUser(User user) {
        LOG.debug("updating user");
        try {
            service.updateUser(user);

            return Response.noContent().build();

        } catch (ServiceException e) {
            LOG.warn("Got service exception: ", e.getSuppliedException());
            throw new InternalServerErrorException();

        } catch (Exception e) {
            LOG.warn("Got general exception ", e);
            throw new InternalServerErrorException();
        }
    }

    @Inject
    public void setService(UserService service) {
        this.service = service;
    }
}
