package org.tiatus.server.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.entity.Club;
import org.tiatus.role.Role;
import org.tiatus.service.ClubService;
import org.tiatus.service.ServiceException;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

/**
 * Created by johnreynolds on 19/06/2016.
 */
@Path("clubs")
@SuppressWarnings("squid:S1166")
public class ClubRestPoint {

    private static final Logger LOG = LoggerFactory.getLogger(ClubRestPoint.class);

    private ClubService service;

    /**
     * Get clubs
     * @return response containing list of clubs
     */
    @PermitAll
    @GET
    @Produces("application/json")
    public Response getClubs() {
        List<Club> clubs = service.getClubs();
        return Response.ok(clubs).build();
    }

    /**
     * Add club, restricted to Admin users
     * @param uriInfo location details
     * @param club to add
     * @return 201 response with location containing uri of newly created club or an error code
     */
    @RolesAllowed({Role.ADMIN})
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response addClub(@Context UriInfo uriInfo, Club club) {
        LOG.debug("Adding club " + club);
        try {
            Club saved = service.addClub(club);
            return Response.created(URI.create(uriInfo.getPath() + "/"+ saved.getId())).build();

        } catch (ServiceException e) {
            LOG.warn("Got service exception: ", e.getSuppliedException());
            throw new InternalServerErrorException();

        } catch (Exception e) {
            LOG.warn("Got general exception ", e);
            throw new InternalServerErrorException();
        }
    }

    /**
     * Remove club, restricted to Admin users
     * @param id of club to remove
     * @return response with 204
     */
    @RolesAllowed({Role.ADMIN})
    @DELETE
    @Path("{id}")
    @Produces("application/json")
    public Response removeClub(@PathParam("id") String id) {
        LOG.debug("Removing club with id " + id);
        try {
            Club club = new Club();
            club.setId(Long.parseLong(id));
            service.deleteClub(club);
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
     * Update club, restricted to Admin users
     * @param club to update
     * @return 200 response or an error code
     */
    @PUT
    @RolesAllowed({Role.ADMIN})
    @Produces("application/json")
    public Response updateClub(Club club) {
        LOG.debug("updating club");
        try {
            service.updateClub(club);

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
    // sonar want constructor injection which jaxrs does not support
    public void setService(ClubService service) {
        this.service = service;
    }
}
