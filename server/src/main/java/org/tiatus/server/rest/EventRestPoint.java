package org.tiatus.server.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.entity.Event;
import org.tiatus.entity.EventPosition;
import org.tiatus.entity.Position;
import org.tiatus.entity.RaceEvent;
import org.tiatus.role.Role;
import org.tiatus.service.EventService;
import org.tiatus.service.PositionService;
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
 * Created by johnreynolds on 29/10/2016.
 */
@Path("events")
@SuppressWarnings("squid:S1166")
public class EventRestPoint {

    private EventService service;
    private PositionService positionService;

    private static final String GENERAL_EXCEPTION = "Got general exception: ";
    private static final Logger LOG = LoggerFactory.getLogger(EventRestPoint.class);

    /**
     * Add event, restricted to Admin users
     * @param uriInfo location details
     * @param event to add
     * @return 201 response with location containing uri of newly created event or an error code
     */
    @POST
    @RolesAllowed({Role.ADMIN})
    @Consumes("application/json")
    @Produces("application/json")
    public Response createEvent(Event event, @Context UriInfo uriInfo) {
        // call service which will place in db and queue
        LOG.debug("Adding event name " + event.getName());
        try {
            for (EventPosition eventPosition: event.getPositions()) {
                Position position = positionService.getPositionForId(eventPosition.getPositionId());
                eventPosition.setPosition(position);
            }
            Event saved = service.addEvent(event);
            return Response.created(URI.create(uriInfo.getPath() + "/"+ saved.getId())).entity(saved).build();

        } catch (ServiceException e) {
            LOG.warn("Got service exception: ", e.getSuppliedException());
            throw new InternalServerErrorException();

        } catch (Exception e) {
            LOG.warn(GENERAL_EXCEPTION, e);
            throw new InternalServerErrorException();
        }
    }

    /**
     * Remove event, restricted to Admin users
     * @param id of event to remove
     * @return response with 204
     */
    @DELETE
    @RolesAllowed({Role.ADMIN})
    @Path("{id}")
    @Produces("application/json")
    public Response removeEvent(@PathParam("id") Long id) {
        // call service which will place in db and queue
        LOG.debug("Got event id " + id);
        Event event = new Event();
        event.setId(id);
        try {
            service.deleteEvent(event);
            return Response.noContent().build();

        } catch (ServiceException e) {
            LOG.warn("Failed to delete event: " + event.getName(), e);
            throw new InternalServerErrorException();
        } catch (Exception e) {
            LOG.warn(GENERAL_EXCEPTION, e);
            throw new InternalServerErrorException();
        }
    }

    /**
     * Remove event, restricted to Admin users
     * @param id of event to remove
     * @return response with 204
     */
    @DELETE
    @RolesAllowed({Role.ADMIN})
    @Path("unassigned/{id}")
    @Produces("application/json")
    public Response removeUnassignedEvent(@PathParam("id") Long id) {
        // call service which will place in db and queue
        LOG.debug("Got event id " + id);
        Event event = new Event();
        event.setId(id);
        try {
            service.deleteEvent(event);
            return Response.noContent().build();

        } catch (ServiceException e) {
            LOG.warn("Failed to delete event: " + event.getName(), e);
            throw new InternalServerErrorException();
        } catch (Exception e) {
            LOG.warn(GENERAL_EXCEPTION, e);
            throw new InternalServerErrorException();
        }
    }

    /**
     * Get events
     * @return response containing list of events
     */
    @GET
    @PermitAll
    @Produces("application/json")
    public Response getEvents() {
        List<Event> events = service.getEvents();
        return Response.ok(events).build();

    }

    /**
     * Get events assinged to races
     * @return response containing list of events assigned to races
     */
    @GET
    @PermitAll
    @Path("assigned")
    @Produces("application/json")
    public Response getAssignedEvents() {
        List<RaceEvent> events = service.getAssignedEvents();
        return Response.ok(events).build();
    }

    /**
     * Remove assigned event, restricted to Admin users
     * @param id of assigned event to remove
     * @return response with 204
     */
    @DELETE
    @RolesAllowed({Role.ADMIN})
    @Path("assigned/{id}")
    @Produces("application/json")
    public Response removeAssignedEvent(@PathParam("id") Long id) {
        LOG.debug("Got raceEvent id " + id);
        RaceEvent raceEvent = new RaceEvent();
        raceEvent.setId(id);
        try {
            service.deleteRaceEvent(raceEvent);
            return Response.noContent().build();

        } catch (ServiceException e) {
            LOG.warn("Failed to delete raceEvent: " + raceEvent.getId(), e);
            throw new InternalServerErrorException();
        } catch (Exception e) {
            LOG.warn(GENERAL_EXCEPTION, e);
            throw new InternalServerErrorException();
        }
    }

    /**
     * Add assigned event, restricted to Admin users
     * @param uriInfo location details
     * @param raceEvent to add
     * @return 201 response with location containing uri of newly created raceEvent or an error code
     */
    @POST
    @RolesAllowed({Role.ADMIN})
    @Path("assigned")
    @Consumes("application/json")
    @Produces("application/json")
    public Response addAssignedEvent(RaceEvent raceEvent, @Context UriInfo uriInfo) {
        try {
            RaceEvent saved = service.addRaceEvent(raceEvent);
            return Response.created(URI.create(uriInfo.getPath() + "/"+ saved.getId())).entity(saved).build();
        } catch (ServiceException e) {
            LOG.warn("Got service exception: ", e.getSuppliedException());
            throw new InternalServerErrorException();

        } catch (Exception e) {
            LOG.warn(GENERAL_EXCEPTION, e);
            throw new InternalServerErrorException();
        }
    }

    /**
     * Update assigned events, restricted to Admin users
     * @param raceEvents list containing race events to update
     * @return 200 response or an error code
     */
    @PUT
    @RolesAllowed({Role.ADMIN})
    @Path("assigned")
    @Produces("application/json")
    public Response updateAssignedEvents(List<RaceEvent> raceEvents) {
        LOG.debug("updating assigned events");
        try {
            service.updateRaceEvents(raceEvents);
        } catch (ServiceException e) {
            LOG.warn("Got service exception: ", e.getSuppliedException());
            throw new InternalServerErrorException();

        } catch (Exception e) {
            LOG.warn(GENERAL_EXCEPTION, e);
            throw new InternalServerErrorException();
        }
        return Response.ok().build();
    }

    /**
     * Get events not assigned to any race
     * @return response containing list of events not assigned to any race
     */
    @GET
    @PermitAll
    @Path("unassigned")
    @Produces("application/json")
    public Response getUnassignedEvents() {
        List<Event> events = service.getUnassignedEvents();
        return Response.ok(events).build();

    }

    @Inject
    // sonar want constructor injection which jaxrs does not support
    public void setService(EventService service) {
        this.service = service;
    }

    @Inject
    public void setPositionService(PositionService service) {
        this.positionService = service;
    }
}
