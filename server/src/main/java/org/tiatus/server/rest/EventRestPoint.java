package org.tiatus.server.rest;

import org.infinispan.Cache;
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
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;

/**
 * Created by johnreynolds on 29/10/2016.
 */
@Path("events")
@SuppressWarnings("squid:S1166")
public class EventRestPoint {
    private static final String GENERAL_EXCEPTION = "Got general exception: ";
    private static final Logger LOG = LoggerFactory.getLogger(EventRestPoint.class);
    private static final String CACHE_NAME = "events";

    private EventService service;
    private PositionService positionService;
    private Cache cache;


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
    public Response createEvent(Event event, @Context HttpServletRequest request, @Context UriInfo uriInfo) {
        // call service which will place in db and queue
        LOG.debug("Adding event name " + event.getName());
        try {
            for (EventPosition eventPosition: event.getPositions()) {
                Position position = positionService.getPositionForId(eventPosition.getPositionId());
                eventPosition.setPosition(position);
            }
            Event saved = service.addEvent(event, request.getSession().getId());
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
     * Update event, restricted to Admin users
     * @param event to update
     * @return 201 response with location containing uri of newly created event or an error code
     */
    @PUT
    @RolesAllowed({Role.ADMIN})
    @Consumes("application/json")
    @Produces("application/json")
    public Response updateEvent(Event event, @Context HttpServletRequest request, @Context UriInfo uriInfo) {
        // call service which will place in db and queue
        LOG.debug("Updating event of id " + event.getId());
        try {
            for (EventPosition eventPosition: event.getPositions()) {
                Position position = positionService.getPositionForId(eventPosition.getPositionId());
                eventPosition.setPosition(position);
            }
            Event saved = service.updateEvent(event, request.getSession().getId());
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
    public Response removeEvent(@PathParam("id") Long id, @Context HttpServletRequest request) {
        // call service which will place in db and queue
        LOG.debug("Got event id " + id);
        Event event = new Event();
        event.setId(id);
        try {
            service.deleteEvent(event, request.getSession().getId());
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
    public Response removeUnassignedEvent(@PathParam("id") Long id, @Context HttpServletRequest request) {
        // call service which will place in db and queue
        LOG.debug("Got event id " + id);
        Event event = new Event();
        event.setId(id);
        try {
            service.deleteEvent(event, request.getSession().getId());
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
    public Response getEvents(@Context Request request) {
        Response.ResponseBuilder builder;
        if (cache.get(CACHE_NAME) != null) {
            CacheEntry cacheEntry = (CacheEntry)cache.get(CACHE_NAME);
            String cachedEntryETag = cacheEntry.getETag();

            EntityTag cachedRacesETag = new EntityTag(cachedEntryETag, false);
            builder = request.evaluatePreconditions(cachedRacesETag);
            if (builder == null) {
                List<Event> events = (List<Event>)cacheEntry.getEntry();
                builder = Response.ok(events).tag(cachedEntryETag);
            }
        } else {
            List<Event> events = service.getEvents();
            String hashCode = Integer.toString(events.hashCode());
            EntityTag etag = new EntityTag(hashCode, false);
            CacheEntry newCacheEntry = new CacheEntry(hashCode, events);
            cache.put(CACHE_NAME, newCacheEntry);
            builder = Response.ok(events).tag(etag);
        }

        return builder.build();
    }

    /**
     * Get events assinged to races
     * @return response containing list of events assigned to races
     */
    @GET
    @PermitAll
    @Path("assigned")
    @Produces("application/json")
    public Response getAssignedEvents(@Context Request request) {
        Response.ResponseBuilder builder;
        String cacheName = CACHE_NAME + "_" + "Assigned";
        if (cache.get(cacheName) != null) {
            CacheEntry cacheEntry = (CacheEntry)cache.get(cacheName);
            String cachedEntryETag = cacheEntry.getETag();

            EntityTag cachedRacesETag = new EntityTag(cachedEntryETag, false);
            builder = request.evaluatePreconditions(cachedRacesETag);
            if (builder == null) {
                List<RaceEvent> events = (List<RaceEvent>)cacheEntry.getEntry();
                builder = Response.ok(events).tag(cachedEntryETag);
            }
        } else {
            List<RaceEvent> events = service.getAssignedEvents();
            String hashCode = Integer.toString(events.hashCode());
            EntityTag etag = new EntityTag(hashCode, false);
            CacheEntry newCacheEntry = new CacheEntry(hashCode, events);
            cache.put(cacheName, newCacheEntry);
            builder = Response.ok(events).tag(etag);
        }

        return builder.build();
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
    public Response removeAssignedEvent(@PathParam("id") Long id, @Context HttpServletRequest request) {
        LOG.debug("Got raceEvent id " + id);
        RaceEvent raceEvent = new RaceEvent();
        raceEvent.setId(id);
        try {
            service.deleteRaceEvent(raceEvent, request.getSession().getId());
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
    public Response addAssignedEvent(RaceEvent raceEvent, @Context HttpServletRequest request, @Context UriInfo uriInfo) {
        try {
            RaceEvent saved = service.addRaceEvent(raceEvent, request.getSession().getId());
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
    public Response updateAssignedEvents(List<RaceEvent> raceEvents, @Context HttpServletRequest request) {
        LOG.debug("updating assigned events");
        try {
            service.updateRaceEvents(raceEvents, request.getSession().getId());
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
    public Response getUnassignedEvents(@Context Request request) {
        Response.ResponseBuilder builder;
        String cacheName = CACHE_NAME + "_" + "Unassigned";
        if (cache.get(cacheName) != null) {
            CacheEntry cacheEntry = (CacheEntry)cache.get(cacheName);
            String cachedEntryETag = cacheEntry.getETag();

            EntityTag cachedRacesETag = new EntityTag(cachedEntryETag, false);
            builder = request.evaluatePreconditions(cachedRacesETag);
            if (builder == null) {
                List<Event> events = (List<Event>)cacheEntry.getEntry();
                builder = Response.ok(events).tag(cachedEntryETag);
            }
        } else {
            List<Event> events = service.getUnassignedEvents();
            String hashCode = Integer.toString(events.hashCode());
            EntityTag etag = new EntityTag(hashCode, false);
            CacheEntry newCacheEntry = new CacheEntry(hashCode, events);
            cache.put(cacheName, newCacheEntry);
            builder = Response.ok(events).tag(etag);
        }

        return builder.build();
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

    @Inject
    public void setCache(Cache cache) {
        this.cache = cache;
    }
}
