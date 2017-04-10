package org.tiatus.server.rest;

import org.infinispan.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.entity.Entry;
import org.tiatus.entity.EntryPositionTime;
import org.tiatus.entity.Position;
import org.tiatus.entity.Race;
import org.tiatus.role.Role;
import org.tiatus.service.*;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;

/**
 * Created by johnreynolds on 08/02/2017.
 */
@Path("time")
public class TimeRestPoint {

    private static final Logger LOG = LoggerFactory.getLogger(TimeRestPoint.class);
    private static final String CACHE_NAME = "times";

    private TimesService service;
    private RaceService raceService;
    private EntryService entryService;
    private PositionService positionService;
    private Cache cache;

    @GET
    @Path("position/{positionId}/race/{raceId}")
    @Produces("application/json")
    public Response getListOfEntriesAndTimesForPositionInRace(@PathParam("positionId") String positionId, @PathParam("raceId") String raceId, @Context Request request) {
        try {
            Race race = raceService.getRaceForId(Long.parseLong(raceId));
            Position position = positionService.getPositionForId(Long.parseLong(positionId));
            if (race == null || position == null) {
                LOG.warn("Failed to get race or position for supplied ids");
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            Response.ResponseBuilder builder;
            String cacheName = CACHE_NAME + "_" + positionId + "_" + raceId;
            if (cache.get(cacheName) != null) {
                CacheEntry cacheEntry = (CacheEntry)cache.get(cacheName);
                String cachedEntryETag = cacheEntry.getETag();

                EntityTag cachedRacesETag = new EntityTag(cachedEntryETag, false);
                builder = request.evaluatePreconditions(cachedRacesETag);
                if (builder == null) {
                    List<EntryPositionTime> times = (List<EntryPositionTime>)cacheEntry.getEntry();
                    builder = Response.ok(times).tag(cachedEntryETag);
                }
            } else {
                List<EntryPositionTime> times = service.getPositionTimesForPositionInRace(race, position);
                String hashCode = Integer.toString(times.hashCode());
                EntityTag etag = new EntityTag(hashCode, false);
                CacheEntry newCacheEntry = new CacheEntry(hashCode, times);
                cache.put(cacheName, newCacheEntry);
                builder = Response.ok(times).tag(etag);
            }

            return builder.build();

        } catch (ServiceException e) {
            LOG.warn("Got service exception: ", e.getSuppliedException());
            throw new InternalServerErrorException();

        } catch (Exception e) {
            LOG.warn("Got general exception ", e);
            throw new InternalServerErrorException();
        }
    }

    @RolesAllowed({Role.TIMING})
    @POST
    @Path("position/{positionId}/entry/{entryId}")
    @Produces("application/json")
    public Response saveTime(@PathParam("positionId") String positionId, @PathParam("entryId") String entryId, EntryPositionTime entryPositionTime, @Context UriInfo uriInfo) {
        try {
            Position position = positionService.getPositionForId(Long.parseLong(positionId));
            entryPositionTime.setPosition(position);
            Entry entry = entryService.getEntryForId(Long.parseLong(entryId));
            entryPositionTime.setEntry(entry);
            if (entry == null || position == null) {
                LOG.warn("Failed to get entry or position for supplied ids");
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            LOG.debug("have entry " + entryPositionTime.getEntry().getId());
            LOG.debug("Have time " + entryPositionTime.getTime());
            service.createTime(entryPositionTime);
            wipeCache(position, entry);
            return Response.created(URI.create(uriInfo.getPath())).build();

        } catch (ServiceException e) {
            LOG.warn("Got service exception: ", e.getSuppliedException());
            throw new InternalServerErrorException();

        } catch (Exception e) {
            LOG.warn("Got general exception ", e);
            throw new InternalServerErrorException();
        }
    }

    @RolesAllowed({Role.TIMING})
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
            wipeCache(position, entry);
            return Response.ok().build();

        } catch (ServiceException e) {
            LOG.warn("Got service exception: ", e.getSuppliedException());
            throw new InternalServerErrorException();

        } catch (Exception e) {
            LOG.warn("Got general exception ", e);
            throw new InternalServerErrorException();
        }
    }

    private void wipeCache(Position position, Entry entry) {
        String cachePositionInRace = CACHE_NAME + "_" + position.getId() + "_" + entry.getRace().getId();
        if (cache.get(cachePositionInRace) != null) {
            cache.evict(cachePositionInRace);
        }
        String cacheFullForRace = CACHE_NAME + "_full_" + entry.getRace().getId();
        if (cache.get(cacheFullForRace) != null) {
            cache.evict(cacheFullForRace);
        }
        String cacheForRace = CACHE_NAME + "_" + entry.getRace().getId();
        if (cache.get(cacheForRace) != null) {
            cache.evict(cacheForRace);
        }
    }

    @GET
    @Path("race/{raceId}")
    @Produces("application/json")
    public Response getListOfTimesForRace(@PathParam("raceId") String raceId, @Context Request request) {
        try {
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
                    List<EntryPositionTime> times = (List<EntryPositionTime>)cacheEntry.getEntry();
                    builder = Response.ok(times).tag(cachedEntryETag);
                }
            } else {
                List<EntryPositionTime> times = service.getTimesForRace(race);
                String hashCode = Integer.toString(times.hashCode());
                EntityTag etag = new EntityTag(hashCode, false);
                CacheEntry newCacheEntry = new CacheEntry(hashCode, times);
                cache.put(cacheName, newCacheEntry);
                builder = Response.ok(times).tag(etag);
            }

            return builder.build();

        } catch (ServiceException e) {
            LOG.warn("Got service exception: ", e.getSuppliedException());
            throw new InternalServerErrorException();

        } catch (Exception e) {
            LOG.warn("Got general exception ", e);
            throw new InternalServerErrorException();
        }
    }

    @RolesAllowed({Role.ADJUDICATOR})
    @GET
    @Path("race/{raceId}/full")
    @Produces("application/json")
    public Response getListOfFullTimesForRace(@PathParam("raceId") String raceId, @Context Request request) {
        try {
            Race race = raceService.getRaceForId(Long.parseLong(raceId));
            if (race == null) {
                LOG.warn("Failed to get race for supplied id");
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            Response.ResponseBuilder builder;
            String cacheName = CACHE_NAME + "_full_" + raceId;
            if (cache.get(cacheName) != null) {
                CacheEntry cacheEntry = (CacheEntry)cache.get(cacheName);
                String cachedEntryETag = cacheEntry.getETag();

                EntityTag cachedRacesETag = new EntityTag(cachedEntryETag, false);
                builder = request.evaluatePreconditions(cachedRacesETag);
                if (builder == null) {
                    List<EntryPositionTime> times = (List<EntryPositionTime>)cacheEntry.getEntry();
                    builder = Response.ok(times).tag(cachedEntryETag);
                }
            } else {
                List<EntryPositionTime> times = service.getAllTimesForRace(race);
                String hashCode = Integer.toString(times.hashCode());
                EntityTag etag = new EntityTag(hashCode, false);
                CacheEntry newCacheEntry = new CacheEntry(hashCode, times);
                cache.put(cacheName, newCacheEntry);
                builder = Response.ok(times).tag(etag);
            }

            return builder.build();

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

    @Inject
    public void setCache(Cache cache) {
        this.cache = cache;
    }
}
