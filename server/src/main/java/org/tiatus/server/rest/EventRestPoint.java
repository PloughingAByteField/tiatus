package org.tiatus.server.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.entity.Event;
import org.tiatus.entity.RaceEvent;
import org.tiatus.role.Role;
import org.tiatus.service.EventService;
import org.tiatus.service.ServiceException;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

@Path("events")
@SuppressWarnings("squid:S1166")
public class EventRestPoint {

    private EventService service;

    private static final Logger LOG = LoggerFactory.getLogger(EventRestPoint.class);

    @POST
    @RolesAllowed({Role.ADMIN})
    @Consumes("application/json")
    @Produces("application/json")
    public Response createEvent(Event event, @Context UriInfo uriInfo) {
        // call service which will place in db and queue
        LOG.debug("Adding event name " + event.getName());
        try {
            Event saved = service.addEvent(event);
            return Response.created(URI.create(uriInfo.getPath() + "/"+ saved.getId())).build();

        } catch (ServiceException e) {
            LOG.warn("Got service exception: ", e.getSuppliedException());
            throw new InternalServerErrorException();

        } catch (Exception e) {
            LOG.warn("Got general exception ", e);
            throw new InternalServerErrorException();
        }
    }

    @DELETE
    @RolesAllowed({Role.ADMIN})
    @Path("{id}")
    @Produces("application/json")
    public Response removeEvent(@PathParam("id") Long id, @Context HttpServletRequest request) {
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
            LOG.warn("Got general exception ", e);
            throw new InternalServerErrorException();
        }
    }

    @GET
    @PermitAll
    @Produces("application/json")
    public Response getEvents(@Context HttpServletRequest request) {
        List<Event> events = service.getEvents();
        return Response.ok(events).build();

    }

    @GET
    @PermitAll
    @Path("assigned")
    @Produces("application/json")
    public Response getAssignedEvents(@Context HttpServletRequest request) {
        List<RaceEvent> events = service.getAssignedEvents();
        return Response.ok(events).build();
    }

    @DELETE
    @RolesAllowed({Role.ADMIN})
    @Path("assigned/{id}")
    @Produces("application/json")
    public Response removeAssignedEvent(@PathParam("id") Long id, @Context HttpServletRequest request) {
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
            LOG.warn("Got general exception ", e);
            throw new InternalServerErrorException();
        }
    }

    @POST
    @RolesAllowed({Role.ADMIN})
    @Path("assigned")
    @Consumes("application/json")
    @Produces("application/json")
    public Response addAssignedEvent(RaceEvent raceEvent, @Context HttpServletRequest request, @Context UriInfo uriInfo) {
        try {
            RaceEvent saved = service.addRaceEvent(raceEvent);
            return Response.created(URI.create(uriInfo.getPath() + "/"+ saved.getId())).build();
        } catch (ServiceException e) {
            LOG.warn("Got service exception: ", e.getSuppliedException());
            throw new InternalServerErrorException();

        } catch (Exception e) {
            LOG.warn("Got general exception ", e);
            throw new InternalServerErrorException();
        }
    }

    @GET
    @PermitAll
    @Path("unassigned")
    @Produces("application/json")
    public Response getUnassignedEvents(@Context HttpServletRequest request) {
        List<Event> events = service.getUnassignedEvents();
        return Response.ok(events).build();

    }

    @Inject
    // sonar want constructor injection which jaxrs does not support
    public void setService(EventService service) {
        this.service = service;
    }
}
