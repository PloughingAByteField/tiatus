package org.tiatus.server.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.entity.Race;
import org.tiatus.service.RaceService;
import org.tiatus.service.ServiceException;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 * Created by johnreynolds on 19/06/2016.
 */
@Path("race")
public class RaceRestPoint {

    private static final Logger LOG = LoggerFactory.getLogger(RaceRestPoint.class);

    @Inject
    private RaceService service;

    @PermitAll
    @Produces("application/json")
    public Response getRaces() {
        return Response.ok().build();
    }

    @PermitAll
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response addRace(Race race) {
        LOG.debug("Adding race " + race);
        try {
            Race saved = service.addRace(race);
            return Response.ok(saved).build();

        } catch (ServiceException e) {
            LOG.warn("Got service exception");
            return Response.serverError().build();
        }

    }
}
