package org.tiatus.server.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.entity.Race;
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

/**
 * Created by johnreynolds on 19/06/2016.
 */
@Path("races")
@SuppressWarnings("squid:S1166")
public class RaceRestPoint {

    private static final Logger LOG = LoggerFactory.getLogger(RaceRestPoint.class);

    private RaceService service;


    @PermitAll
    @GET
    @Produces("application/json")
    public Response getRaces() {
        return Response.ok().build();
    }

    @PermitAll
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

    @RolesAllowed({"role1", "role2"})
    @GET
    @Path("other")
    @Produces("application/json")
    public Response getRacesOther() {
        return Response.ok().build();
    }

    @Inject
    // sonar want constructor injection which jaxrs does not support
    public void setService(RaceService service) {
        this.service = service;
    }
}
