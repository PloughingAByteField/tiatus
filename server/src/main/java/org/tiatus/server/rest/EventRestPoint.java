package org.tiatus.server.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.entity.Event;
import org.tiatus.service.EventService;
import org.tiatus.service.RaceService;
import org.tiatus.service.ServiceException;

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

    @Inject
    private EventService service;

    private static final Logger LOG = LoggerFactory.getLogger(EventRestPoint.class);

    @POST
    @Produces("application/json")
    public Response createEvent(Event event, @Context UriInfo uriInfo) {
        // call service which will place in db and queue
        LOG.debug("Adding event name " + event.getName());
        try {
            LOG.debug("calling service");
            Event saved = service.addEvent(event);
            return Response.created(URI.create(uriInfo.getPath() + "/"+ saved.getId())).entity(saved).build();

        } catch (ServiceException e) {
            LOG.warn("Got service exception: ", e.getSuppliedException());
            throw new InternalServerErrorException();

        } catch (Exception e) {
            LOG.warn("Got general exception ", e);
            throw new InternalServerErrorException();
        }
    }

    @DELETE
    @Path("{id}")
    @Produces("application/json")
    public Response removeEvent(@PathParam("id") Long id, @Context HttpServletRequest request) {
        // call service which will place in db and queue
        LOG.debug("Got event id " + id);
        Event event = new Event();
        event.setId(id);
        try {
            service.deleteEvent(event);
            return Response.ok().build();

        } catch (ServiceException e) {
            LOG.warn("Failed to delete event: " + event.getName(), e);
            return Response.status(Status.BAD_REQUEST).build();
        }
    }

    @GET
    @Produces("application/json")
    public Response getEvents(@Context HttpServletRequest request) {
        LOG.debug("calling service");
        List<Event> events = service.getEvents();
        return Response.ok(events).build();

    }

    @Inject
    // sonar want constructor injection which jaxrs does not support
    public void setService(EventService service) {
        this.service = service;
    }
}
