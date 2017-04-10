package org.tiatus.server.rest;

import org.infinispan.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.entity.Entry;
import org.tiatus.entity.Race;
import org.tiatus.role.Role;
import org.tiatus.service.EntryService;
import org.tiatus.service.RaceService;
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
 * Created by johnreynolds on 19/06/2016.
 */
@Path("entries")
@SuppressWarnings("squid:S1166")
public class EntryRestPoint {

    private static final Logger LOG = LoggerFactory.getLogger(EntryRestPoint.class);
    private static final String CACHE_NAME = "entries";

    private EntryService service;
    private RaceService raceService;
    private Cache cache;

    /**
     * Get entries
     * @return response containing list of entries
     */
    @PermitAll
    @GET
    @Produces("application/json")
    public Response getEntries(@Context Request request) {
        Response.ResponseBuilder builder;
        if (cache.get(CACHE_NAME) != null) {
            CacheEntry cacheEntry = (CacheEntry)cache.get(CACHE_NAME);
            String cachedEntryETag = cacheEntry.getETag();

            EntityTag cachedRacesETag = new EntityTag(cachedEntryETag, false);
            builder = request.evaluatePreconditions(cachedRacesETag);
            if (builder == null) {
                List<Entry> entries = (List<Entry>)cacheEntry.getEntry();
                builder = Response.ok(entries).tag(cachedEntryETag);
            }
        } else {
            List<Entry> entries = service.getEntries();
            String hashCode = Integer.toString(entries.hashCode());
            EntityTag etag = new EntityTag(hashCode, false);
            CacheEntry newCacheEntry = new CacheEntry(hashCode, entries);
            cache.put(CACHE_NAME, newCacheEntry);
            builder = Response.ok(entries).tag(etag);
        }

        return builder.build();
    }

    @PermitAll
    @GET
    @Path("race/{raceId}")
    @Produces("application/json")
    public Response getEntriesForRace(@PathParam("raceId") String raceId, @Context Request request) {
        Race race = raceService.getRaceForId(Long.parseLong(raceId));
        if (race == null) {
            LOG.warn("Failed to get race for supplied id");
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Response.ResponseBuilder builder;
        String cacheName = CACHE_NAME + "_" + raceId;
        if (cache.get(cacheName) != null) {
            CacheEntry cacheEntry = (CacheEntry)cache.get(cacheName);
            String cachedEntryETag = cacheEntry.getETag();

            EntityTag cachedRacesETag = new EntityTag(cachedEntryETag, false);
            builder = request.evaluatePreconditions(cachedRacesETag);
            if (builder == null) {
                List<Entry> entries = (List<Entry>)cacheEntry.getEntry();
                builder = Response.ok(entries).tag(cachedEntryETag);
            }
        } else {
            List<Entry> entries = service.getEntriesForRace(race);
            String hashCode = Integer.toString(entries.hashCode());
            EntityTag etag = new EntityTag(hashCode, false);
            CacheEntry newCacheEntry = new CacheEntry(hashCode, entries);
            cache.put(cacheName, newCacheEntry);
            builder = Response.ok(entries).tag(etag);
        }

        return builder.build();
    }

    /**
     * Add entry, restricted to Admin users
     * @param uriInfo location details
     * @param entry to add
     * @return 201 response with location containing uri of newly created entry or an error code
     */
    @RolesAllowed({Role.ADMIN})
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response addEntry(@Context UriInfo uriInfo, @Context HttpServletRequest request, Entry entry) {
        LOG.debug("Adding entry " + entry);
        try {
            Entry saved = service.addEntry(entry, request.getSession().getId());
            if (cache.get(CACHE_NAME) != null) {
                cache.evict(CACHE_NAME);
            }
            return Response.created(URI.create(uriInfo.getPath() + "/"+ saved.getId())).build();

        } catch (ServiceException e) {
            LOG.warn("Got service exception: ", e.getSuppliedException());
            throw new InternalServerErrorException();

        } catch (Exception e) {
            LOG.warn("Got general exception ", e);
            throw new InternalServerErrorException();
        }
    }

    /**
     * Remove entry, restricted to Admin users
     * @param id of entry to remove
     * @return response with 204
     */
    @RolesAllowed({Role.ADMIN})
    @DELETE
    @Path("{id}")
    @Produces("application/json")
    public Response removeEntry(@PathParam("id") String id, @Context HttpServletRequest request) {
        LOG.debug("Removing entry with id " + id);
        try {
            Entry entry = new Entry();
            entry.setId(Long.parseLong(id));
            service.deleteEntry(entry, request.getSession().getId());
            if (cache.get(CACHE_NAME) != null) {
                cache.evict(CACHE_NAME);
            }
            return Response.noContent().build();

        } catch (ServiceException e) {
            LOG.warn("Got service exception: ", e.getSuppliedException());
            throw new InternalServerErrorException();

        } catch (Exception e) {
            LOG.warn("Got general exception ", e);
            throw new InternalServerErrorException();
        }
    }

    /**
     * Update entry, restricted to Admin users
     * @param entry to update
     * @return 404 if entity does not exist else return 204
     */
    @RolesAllowed({Role.ADMIN})
    @PUT
    @Path("{id}")
    @Consumes("application/json")
    @Produces("application/json")
    public Response updateEntry(@PathParam("id") String id, @Context HttpServletRequest request, Entry entry) {
        LOG.debug("Updating entry " + id);
        try {
            Entry existing = service.getEntryForId(Long.parseLong(id));
            if (existing == null) {
                LOG.warn("Failed to get entry for supplied id");
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            service.updateEntry(entry, request.getSession().getId());
            if (cache.get(CACHE_NAME) != null) {
                cache.evict(CACHE_NAME);
            }
            return Response.noContent().build();

        } catch (ServiceException e) {
            LOG.warn("Got service exception: ", e.getSuppliedException());
            throw new InternalServerErrorException();

        } catch (Exception e) {
            LOG.warn("Got general exception ", e);
            throw new InternalServerErrorException();
        }
    }

    /**
     * Update list of entries, restricted to Admin users
     * @param entries to update
     * @return 201 response with location containing uri of newly created entry or an error code
     */
    @RolesAllowed({Role.ADMIN})
    @PUT
    @Path("updates")
    @Consumes("application/json")
    @Produces("application/json")
    public Response updateEntries(@Context HttpServletRequest request, List<Entry> entries) {
        LOG.debug("Updating " + entries.size() + " entries");
        try {
            service.updateEntries(entries, request.getSession().getId());
            if (cache.get(CACHE_NAME) != null) {
                cache.evict(CACHE_NAME);
            }
            return Response.noContent().build();

        } catch (ServiceException e) {
            LOG.warn("Got service exception: ", e.getSuppliedException());
            throw new InternalServerErrorException();

        } catch (Exception e) {
            LOG.warn("Got general exception ", e);
            throw new InternalServerErrorException();
        }
    }

    @RolesAllowed({Role.ADJUDICATOR})
    @POST
    @Path("swapEntries/{fromId}/{toId}")
    @Produces("application/json")
    public Response swapEntries(@PathParam("fromId") String fromId, @PathParam("toId") String toId, @Context HttpServletRequest request) {
        try {
            Entry from = service.getEntryForId(Long.parseLong(fromId));
            Entry to = service.getEntryForId(Long.parseLong(toId));
            service.swapEntryNumbers(from, to, request.getSession().getId());
            if (cache.get(CACHE_NAME) != null) {
                cache.evict(CACHE_NAME);
            }
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
    // sonar want constructor injection which jaxrs does not support
    public void setService(EntryService service) {
        this.service = service;
    }

    @Inject
    public void setRaceService(RaceService service) {
        this.raceService = service;
    }

    @Inject
    public void setCache(Cache cache) {
        this.cache = cache;
    }
}
