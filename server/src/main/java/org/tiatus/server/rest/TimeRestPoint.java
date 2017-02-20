package org.tiatus.server.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.entity.Entry;
import org.tiatus.entity.EntryPositionTime;
import org.tiatus.entity.Position;
import org.tiatus.entity.Race;
import org.tiatus.service.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

/**
 * Created by johnreynolds on 08/02/2017.
 */
@Path("time")
public class TimeRestPoint {

    private static final Logger LOG = LoggerFactory.getLogger(TimeRestPoint.class);

    private TimesService service;
    private RaceService raceService;
    private EntryService entryService;
    private PositionService positionService;

    @GET
    @Path("position/{positionId}/race/{raceId}")
    @Produces("application/json")
    public Response getListOfEntriesAndTimesForPositionInRace(@PathParam("positionId") String positionId, @PathParam("raceId") String raceId, @Context HttpServletRequest request) {
        try {
            Race race = raceService.getRaceForId(Long.parseLong(raceId));
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

    @POST
    @Path("position/{positionId}/entry/{entryId}")
    @Produces("application/json")
    public Response saveTime(@PathParam("positionId") String positionId, @PathParam("entryId") String entryId, EntryPositionTime entryPositionTime, @Context UriInfo uriInfo) {
        try {
            Position position = positionService.getPositionForId(Long.parseLong(positionId));
            entryPositionTime.setPosition(position);
            Entry entry = entryService.getEntryForId(Long.parseLong(entryId));
            entryPositionTime.setEntry(entry);
            LOG.debug("have entry " + entryPositionTime.getEntry().getId());
            LOG.debug("Have time " + entryPositionTime.getTime());
            service.createTime(entryPositionTime);
            return Response.created(URI.create(uriInfo.getPath())).build();

        } catch (ServiceException e) {
            LOG.warn("Got service exception: ", e.getSuppliedException());
            throw new InternalServerErrorException();

        } catch (Exception e) {
            LOG.warn("Got general exception ", e);
            throw new InternalServerErrorException();
        }
    }

    @PUT
    @Path("position/{positionId}/entry/{entryId}")
    @Produces("application/json")
    public Response updateTime(@PathParam("positionId") String positionId, @PathParam("entryId") String entryId, EntryPositionTime entryPositionTime, @Context HttpServletRequest request) {
        try {
            Position position = positionService.getPositionForId(Long.parseLong(positionId));
            entryPositionTime.setPosition(position);
            Entry entry = entryService.getEntryForId(Long.parseLong(entryId));
            entryPositionTime.setEntry(entry);
            service.updateTime(entryPositionTime);

            return Response.ok().build();

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
    public void setEntryService(EntryService service) {
        this.entryService = service;
    }

    @Inject
    public void setPositionService(PositionService service) {
        this.positionService = service;
    }
}
