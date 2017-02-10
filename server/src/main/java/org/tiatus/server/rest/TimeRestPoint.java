package org.tiatus.server.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.entity.EntryPositionTime;
import org.tiatus.entity.Position;
import org.tiatus.entity.Race;
import org.tiatus.service.PositionService;
import org.tiatus.service.RaceService;
import org.tiatus.service.ServiceException;
import org.tiatus.service.TimesService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by johnreynolds on 08/02/2017.
 */
@Path("time")
public class TimeRestPoint {

    private static final Logger LOG = LoggerFactory.getLogger(TimeRestPoint.class);

    private TimesService service;
    private RaceService raceService;
    private PositionService positionService;

    @GET
    @Path("position/{positionId}/race/{raceId}")
    @Produces("application/json")
    public Response getListOfEntriesAndTimesForPositionInRace(@PathParam("positionId") String positionId, @PathParam("raceId") String raceId, @Context HttpServletRequest request) {
        try {
            Race race = raceService .getRaceForId(Long.parseLong(raceId));
            Position position = positionService.getPositionForId(Long.parseLong(positionId));
            List<EntryPositionTime> times = service.getPositionTimesForPositionInRace(race, position);

            return Response.ok(times).build();

        } catch (ServiceException e) {
            LOG.warn("Got service exception: ", e.getSuppliedException());
            throw new InternalServerErrorException();

        } catch (Exception e) {
            LOG.warn("Got general exception ", e);
            throw new InternalServerErrorException();
        }
    }

    @Inject
    public void setService(TimesService service) {
        this.service = service;
    }

    @Inject
    public void setRaceService(RaceService service) {
        this.raceService = service;
    }

    @Inject
    public void setPositionService(PositionService service) {
        this.positionService = service;
    }
}
