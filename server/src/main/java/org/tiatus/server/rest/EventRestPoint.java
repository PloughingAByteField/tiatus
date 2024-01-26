package org.tiatus.server.rest;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.tiatus.entity.Event;
// import org.tiatus.entity.EventPosition;
// import org.tiatus.entity.Position;
// import org.tiatus.entity.RaceEvent;
// import org.tiatus.role.Role;
// import org.tiatus.service.EventService;
// import org.tiatus.service.PositionService;

// import javax.annotation.security.PermitAll;
// import javax.annotation.security.RolesAllowed;
// import javax.inject.Inject;
// import javax.servlet.http.HttpServletRequest;
// import javax.ws.rs.*;
// import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;

/**
 * Created by johnreynolds on 29/10/2016.
 */
// @Path("events")
// @SuppressWarnings("squid:S1166")
public class EventRestPoint extends RestBase {
    // private static final Logger LOG = LoggerFactory.getLogger(EventRestPoint.class);

    // private EventService service;
    // private PositionService positionService;


    // /**
    //  * Add event, restricted to Admin users
    //  * @param uriInfo location details
    //  * @param event to add
    //  * @return 201 response with location containing uri of newly created event or an error code
    //  */
    // @POST
    // @RolesAllowed({Role.ADMIN})
    // @Consumes("application/json")
    // @Produces("application/json")
    // public Response createEvent(Event event, @Context HttpServletRequest request, @Context UriInfo uriInfo) {
    //     // call service which will place in db and queue
    //     LOG.debug("Adding event name " + event.getName());
    //     try {
    //         for (EventPosition eventPosition: event.getPositions()) {
    //             Position position = positionService.getPositionForId(eventPosition.getPositionId());
    //             eventPosition.setPosition(position);
    //         }
    //         Event saved = service.addEvent(event, request.getSession().getId());
    //         return Response.created(URI.create(uriInfo.getPath() + "/"+ saved.getId())).entity(saved).build();

    //     } catch (Exception e) {
    //         return logError(e);
    //     }
    // }

    // /**
    //  * Update event, restricted to Admin users
    //  * @param id of event to update
    //  * @param event to update
    //  * @return 201 response with location containing uri of newly created event or an error code
    //  */
    // @PUT
    // @Path("{id}")
    // @RolesAllowed({Role.ADMIN})
    // @Consumes("application/json")
    // @Produces("application/json")
    // public Response updateEvent(@PathParam("id") Long id, Event event, @Context HttpServletRequest request, @Context UriInfo uriInfo) {
    //     // call service which will place in db and queue
    //     LOG.debug("Updating event of id " + event.getId());
    //     Event existing = service.getEventForId(id);
    //     if (existing == null) {
    //         LOG.warn("Failed to get event for supplied id");
    //         return Response.status(Response.Status.NOT_FOUND).build();
    //     }

    //     try {
    //         for (EventPosition eventPosition: event.getPositions()) {
    //             Position position = positionService.getPositionForId(eventPosition.getPositionId());
    //             eventPosition.setPosition(position);
    //         }
    //         Event saved = service.updateEvent(event, request.getSession().getId());
    //         return Response.created(URI.create(uriInfo.getPath() + "/"+ saved.getId())).entity(saved).build();

    //     } catch (Exception e) {
    //         return logError(e);
    //     }
    // }

    // /**
    //  * Remove event, restricted to Admin users
    //  * @param id of event to remove
    //  * @return response with 204
    //  */
    // @DELETE
    // @RolesAllowed({Role.ADMIN})
    // @Path("{id}")
    // @Produces("application/json")
    // public Response removeEvent(@PathParam("id") Long id, @Context HttpServletRequest request) {
    //     // call service which will place in db and queue
    //     LOG.debug("Got event id " + id);
    //     try {
    //         Event event = service.getEventForId(id);
    //         if (event != null) {
    //             service.deleteEvent(event, request.getSession().getId());
    //         }
    //         return Response.noContent().build();

    //     } catch (Exception e) {
    //         return logError(e);
    //     }
    // }

    // /**
    //  * Remove event, restricted to Admin users
    //  * @param id of event to remove
    //  * @return response with 204
    //  */
    // @DELETE
    // @RolesAllowed({Role.ADMIN})
    // @Path("unassigned/{id}")
    // @Produces("application/json")
    // public Response removeUnassignedEvent(@PathParam("id") Long id, @Context HttpServletRequest request) {
    //     // call service which will place in db and queue
    //     LOG.debug("Got event id " + id);
    //     try {
    //         Event event = service.getEventForId(id);
    //         if (event != null) {
    //             service.deleteEvent(event, request.getSession().getId());
    //         }
    //         return Response.noContent().build();

    //     } catch (Exception e) {
    //         return logError(e);
    //     }
    // }

    // /**
    //  * Get events
    //  * @return response containing list of events
    //  */
    // @GET
    // @PermitAll
    // @Produces("application/json")
    // public Response getEvents(@Context Request request) {
    //     List<Event> events = service.getEvents();
    //     return Response.ok(events).build();
    // }

    // /**
    //  * Get events assigned to races
    //  * @return response containing list of events assigned to races
    //  */
    // @GET
    // @PermitAll
    // @Path("assigned")
    // @Produces("application/json")
    // public Response getAssignedEvents(@Context Request request) {
    //     List<RaceEvent> events = service.getAssignedEvents();
    //     return Response.ok(events).build();
    // }

    // /**
    //  * Remove assigned event, restricted to Admin users
    //  * @param id of assigned event to remove
    //  * @return response with 204
    //  */
    // @DELETE
    // @RolesAllowed({Role.ADMIN})
    // @Path("assigned/{id}")
    // @Produces("application/json")
    // public Response removeAssignedEvent(@PathParam("id") Long id, @Context HttpServletRequest request) {
    //     LOG.debug("Got raceEvent id " + id);
    //     try {
    //         RaceEvent raceEvent = service.getRaceEventForId(id);
    //         if (raceEvent != null) {
    //             service.deleteRaceEvent(raceEvent, request.getSession().getId());
    //         }
    //         return Response.noContent().build();

    //     } catch (Exception e) {
    //         return logError(e);
    //     }
    // }

    // /**
    //  * Add assigned event, restricted to Admin users
    //  * @param uriInfo location details
    //  * @param raceEvent to add
    //  * @return 201 response with location containing uri of newly created raceEvent or an error code
    //  */
    // @POST
    // @RolesAllowed({Role.ADMIN})
    // @Path("assigned")
    // @Consumes("application/json")
    // @Produces("application/json")
    // public Response addAssignedEvent(RaceEvent raceEvent, @Context HttpServletRequest request, @Context UriInfo uriInfo) {
    //     try {
    //         RaceEvent saved = service.addRaceEvent(raceEvent, request.getSession().getId());
    //         return Response.created(URI.create(uriInfo.getPath() + "/"+ saved.getId())).entity(saved).build();

    //     } catch (Exception e) {
    //         return logError(e);
    //     }
    // }

    // /**
    //  * Update assigned events, restricted to Admin users
    //  * @param raceEvents list containing race events to update
    //  * @return 200 response or an error code
    //  */
    // @PUT
    // @RolesAllowed({Role.ADMIN})
    // @Path("assigned")
    // @Consumes("application/json")
    // @Produces("application/json")
    // public Response updateAssignedEvents(List<RaceEvent> raceEvents, @Context HttpServletRequest request) {
    //     LOG.debug("updating assigned events");
    //     try {
    //         for (RaceEvent raceEventToUpdate : raceEvents) {
    //             RaceEvent existing = service.getRaceEventForId(raceEventToUpdate.getId());
    //             if (existing == null) {
    //                 LOG.warn("Failed to get race event for supplied id");
    //                 return Response.status(Response.Status.NOT_FOUND).build();
    //             }
    //         }

    //         service.updateRaceEvents(raceEvents, request.getSession().getId());

    //     } catch (Exception e) {
    //         return logError(e);
    //     }
    //     return Response.ok().build();
    // }

    // /**
    //  * Get events not assigned to any race
    //  * @return response containing list of events not assigned to any race
    //  */
    // @GET
    // @PermitAll
    // @Path("unassigned")
    // @Produces("application/json")
    // public Response getUnassignedEvents(@Context Request request) {
    //     List<Event> events = service.getUnassignedEvents();
    //     return Response.ok(events).build();
    // }

    // @Inject
    // // sonar want constructor injection which jaxrs does not support
    // public void setService(EventService service) {
    //     this.service = service;
    // }

    // @Inject
    // public void setPositionService(PositionService service) {
    //     this.positionService = service;
    // }

}
