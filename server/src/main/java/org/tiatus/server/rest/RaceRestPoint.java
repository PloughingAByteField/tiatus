package org.tiatus.server.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.entity.Race;
import org.tiatus.role.Role;
import org.tiatus.service.RaceService;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;

/**
 * Created by johnreynolds on 19/06/2016.
 */
@Path("races")
@SuppressWarnings("squid:S1166")
public class RaceRestPoint extends RestBase {

    private static final Logger LOG = LoggerFactory.getLogger(RaceRestPoint.class);

    private RaceService service;

    /**
     * Get races
     * @return response containing list of races
     */
    @PermitAll
    @GET
    @Produces("application/json")
    public Response getRaces(@Context Request request) {
        List<Race> races = service.getRaces();
        return Response.ok(races).build();
    }

    /**
     * Add race, restricted to Admin users
     * @param uriInfo location details
     * @param race to add
     * @return 201 response with location containing uri of newly created race or an error code
     */
    @RolesAllowed({Role.ADMIN})
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response addRace(@Context UriInfo uriInfo, @Context HttpServletRequest request, Race race) {
        LOG.debug("Adding race " + race);
        try {
            Race saved = service.addRace(race, request.getSession().getId());
            return Response.created(URI.create(uriInfo.getPath() + "/"+ saved.getId())).entity(saved).build();

        } catch (Exception e) {
            return logError(e);
        }
    }

    /**
     * Remove race, restricted to Admin users
     * @param id of race to remove
     * @return response with 204
     */
    @RolesAllowed({Role.ADMIN})
    @DELETE
    @Path("{id}")
    @Produces("application/json")
    public Response removeRace(@PathParam("id") Long id, @Context HttpServletRequest request) {
        LOG.debug("Removing race with id " + id);
        try {
            Race race = service.getRaceForId(id);
            if (race != null) {
                service.deleteRace(race, request.getSession().getId());
            }
            return Response.noContent().build();

        } catch (Exception e) {
            return logError(e);
        }
    }

    /**
     * Update race, restricted to Adjudicator and Admin users
     * @param id of race to update
     * @param race of race to update
     * @return response with 204
     */
    @RolesAllowed({Role.ADMIN, Role.ADJUDICATOR})
    @PUT
    @Path("{id}")
    @Produces("application/json")
    public Response updateRace(@PathParam("id") Long id, @Context HttpServletRequest request, Race race) {
        LOG.debug("Updating race with id " + id);
        try {
            Race existing = service.getRaceForId(id);
            if (existing == null) {
                LOG.warn("Failed to get race for supplied id");
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            service.updateRace(race, request.getSession().getId());
            return Response.noContent().build();

        } catch (Exception e) {
            return logError(e);
        }
    }

    @Inject
    // sonar want constructor injection which jaxrs does not support
    public void setService(RaceService service) {
        this.service = service;
    }
}
