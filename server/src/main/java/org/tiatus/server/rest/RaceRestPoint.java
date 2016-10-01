package org.tiatus.server.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.entity.Race;
import org.tiatus.role.Role;
import org.tiatus.service.RaceService;
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
@Path("races")
@SuppressWarnings("squid:S1166")
public class RaceRestPoint {

    private static final Logger LOG = LoggerFactory.getLogger(RaceRestPoint.class);

    private RaceService service;

    /**
     * Get races
     * @return response containing list of races
     */
    @PermitAll
    @GET
    @Produces("application/json")
    public Response getRaces() {
        List<Race> races = service.getRaces();
        return Response.ok(races).build();
    }

    /**
     * Add race, restricted to Admin users
     * @param uriInfo location details
     * @param race to add
     * @return response containg uri of newly created race or an error code
     */
    @RolesAllowed({Role.ADMIN})
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response addRace(@Context UriInfo uriInfo, Race race) {
        LOG.debug("Adding race " + race);
        try {
            Race saved = service.addRace(race);
            return Response.created(URI.create(uriInfo.getPath() + "/"+ saved.getId())).entity(saved).build();

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
    public void setService(RaceService service) {
        this.service = service;
    }
}
