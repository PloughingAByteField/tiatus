package org.tiatus.server.rest;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.tiatus.entity.Entry;
// import org.tiatus.entity.EntryPositionTime;
// import org.tiatus.entity.Position;
// import org.tiatus.entity.Race;
// import org.tiatus.role.Role;
// import org.tiatus.service.*;

// import javax.annotation.security.RolesAllowed;
// import javax.inject.Inject;
// import javax.servlet.http.HttpServletRequest;
// import javax.ws.rs.*;
// import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;

/**
 * Created by johnreynolds on 08/02/2017.
 */
// @Path("time")
public class TimeRestPoint extends RestBase {

    // private static final Logger LOG = LoggerFactory.getLogger(TimeRestPoint.class);

    // private TimesService service;
    // private RaceService raceService;
    // private EntryService entryService;
    // private PositionService positionService;

    // @GET
    // @Path("position/{positionId}/race/{raceId}")
    // @Produces("application/json")
    // public Response getListOfEntriesAndTimesForPositionInRace(@PathParam("positionId") String positionId, @PathParam("raceId") String raceId, @Context Request request) {
    //     try {
    //         Race race = raceService.getRaceForId(Long.parseLong(raceId));
    //         Position position = positionService.getPositionForId(Long.parseLong(positionId));
    //         if (race == null || position == null) {
    //             LOG.warn("Failed to get race or position for supplied ids");
    //             return Response.status(Response.Status.NOT_FOUND).build();
    //         }

    //         List<EntryPositionTime> times = service.getPositionTimesForPositionInRace(race, position);
    //         return Response.ok(times).build();

    //     } catch (Exception e) {
    //         return logError(e);
    //     }
    // }

    // @RolesAllowed({Role.TIMING})
    // @POST
    // @Path("position/{positionId}/entry/{entryId}")
    // @Produces("application/json")
    // public Response saveTime(@PathParam("positionId") String positionId, @PathParam("entryId") String entryId, EntryPositionTime entryPositionTime, @Context UriInfo uriInfo, @Context HttpServletRequest request) {
    //     try {
    //         Position position = positionService.getPositionForId(Long.parseLong(positionId));
    //         entryPositionTime.setPosition(position);
    //         Entry entry = entryService.getEntryForId(Long.parseLong(entryId));
    //         entryPositionTime.setEntry(entry);
    //         if (entry == null || position == null) {
    //             LOG.warn("Failed to get entry or position for supplied ids");
    //             return Response.status(Response.Status.NOT_FOUND).build();
    //         }

    //         LOG.debug("have entry " + entryPositionTime.getEntry().getId());
    //         LOG.debug("Have time " + entryPositionTime.getTime());
    //         service.createTime(entryPositionTime, request.getSession().getId());
    //         return Response.created(URI.create(uriInfo.getPath())).build();

    //     } catch (Exception e) {
    //         return logError(e);
    //     }
    // }

    // @RolesAllowed({Role.TIMING})
    // @PUT
    // @Path("position/{positionId}/entry/{entryId}")
    // @Produces("application/json")
    // public Response updateTime(@PathParam("positionId") String positionId, @PathParam("entryId") String entryId, EntryPositionTime entryPositionTime, @Context HttpServletRequest request) {
    //     try {
    //         Position position = positionService.getPositionForId(Long.parseLong(positionId));
    //         entryPositionTime.setPosition(position);
    //         Entry entry = entryService.getEntryForId(Long.parseLong(entryId));
    //         entryPositionTime.setEntry(entry);
    //         service.updateTime(entryPositionTime, request.getSession().getId());
    //         return Response.ok().build();

    //     } catch (Exception e) {
    //         return logError(e);
    //     }
    // }

    // @GET
    // @Path("race/{raceId}")
    // @Produces("application/json")
    // public Response getListOfTimesForRace(@PathParam("raceId") String raceId, @Context Request request) {
    //     try {
    //         Race race = raceService.getRaceForId(Long.parseLong(raceId));
    //         if (race == null) {
    //             LOG.warn("Failed to get race for supplied id");
    //             return Response.status(Response.Status.NOT_FOUND).build();
    //         }

    //         List<EntryPositionTime> times = service.getTimesForRace(race);
    //         return Response.ok(times).build();

    //     } catch (Exception e) {
    //         return logError(e);
    //     }
    // }

    // @RolesAllowed({Role.ADJUDICATOR})
    // @GET
    // @Path("race/{raceId}/full")
    // @Produces("application/json")
    // public Response getListOfFullTimesForRace(@PathParam("raceId") String raceId, @Context Request request) {
    //     try {
    //         Race race = raceService.getRaceForId(Long.parseLong(raceId));
    //         if (race == null) {
    //             LOG.warn("Failed to get race for supplied id");
    //             return Response.status(Response.Status.NOT_FOUND).build();
    //         }

    //         List<EntryPositionTime> times = service.getAllTimesForRace(race);
    //         return Response.ok(times).build();

    //     } catch (Exception e) {
    //         return logError(e);
    //     }
    // }

    // @Inject
    // public void setService(TimesService service) {
    //     this.service = service;
    // }

    // @Inject
    // public void setRaceService(RaceService service) {
    //     this.raceService = service;
    // }

    // @Inject
    // public void setEntryService(EntryService service) {
    //     this.entryService = service;
    // }

    // @Inject
    // public void setPositionService(PositionService service) {
    //     this.positionService = service;
    // }

}
