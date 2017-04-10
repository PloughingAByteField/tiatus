package org.tiatus.server.rest;

import org.infinispan.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.entity.Race;
import org.tiatus.role.Role;
import org.tiatus.service.RaceService;
import org.tiatus.service.ServiceException;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.Date;
import java.util.List;

/**
 * Created by johnreynolds on 19/06/2016.
 */
@Path("races")
@SuppressWarnings("squid:S1166")
public class RaceRestPoint {

    private static final Logger LOG = LoggerFactory.getLogger(RaceRestPoint.class);
    private static final String CACHE_NAME = "races";

    private RaceService service;
    private Cache cache;

    /**
     * Get races
     * @return response containing list of races
     */
    @PermitAll
    @GET
    @Produces("application/json")
    public Response getRaces(@Context Request request) {
        Response.ResponseBuilder builder;
        if (cache.get(CACHE_NAME) != null) {
            CacheEntry cacheEntry = (CacheEntry)cache.get(CACHE_NAME);
            String cachedEntryETag = cacheEntry.getETag();

            EntityTag cachedRacesETag = new EntityTag(cachedEntryETag, false);
            builder = request.evaluatePreconditions(cachedRacesETag);
            if (builder == null) {
                List<Race> races = (List<Race>)cacheEntry.getEntry();
                builder = Response.ok(races).tag(cachedEntryETag);
            }
        } else {
            List<Race> races = service.getRaces();
            String hashCode = Integer.toString(races.hashCode());
            EntityTag etag = new EntityTag(hashCode, false);
            CacheEntry newCacheEntry = new CacheEntry(hashCode, races);
            cache.put(CACHE_NAME, newCacheEntry);
            builder = Response.ok(races).tag(etag);
        }

        return builder.build();
    }

    /**
     * Add race, restricted to Admin users
     * @param uriInfo location details
     * @param race to add
     * @return 201 response with location containing uri of newly created race or an error code
     */
    @RolesAllowed({Role.ADMIN})
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response addRace(@Context UriInfo uriInfo, @Context HttpServletRequest request, Race race) {
        LOG.debug("Adding race " + race);
        try {
            Race saved = service.addRace(race, request.getSession().getId());
            if (cache.get(CACHE_NAME) != null) {
                cache.evict(CACHE_NAME);
            }
            return Response.created(URI.create(uriInfo.getPath() + "/"+ saved.getId())).entity(saved).build();

        } catch (ServiceException e) {
            LOG.warn("Got service exception: ", e.getSuppliedException());
            throw new InternalServerErrorException();

        } catch (Exception e) {
            LOG.warn("Got general exception ", e);
            throw new InternalServerErrorException();
        }
    }

    /**
     * Remove race, restricted to Admin users
     * @param id of race to remove
     * @return response with 204
     */
    @RolesAllowed({Role.ADMIN})
    @DELETE
    @Path("{id}")
    @Produces("application/json")
    public Response removeRace(@PathParam("id") String id, @Context HttpServletRequest request) {
        LOG.debug("Removing race with id " + id);
        try {
            Race race = new Race();
            race.setId(Long.parseLong(id));
            service.deleteRace(race, request.getSession().getId());
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
     * Update race, restricted to Adjudicator and Admin users
     * @param race of race to update
     * @return response with 204
     */
    @RolesAllowed({Role.ADMIN, Role.ADJUDICATOR})
    @PUT
    @Produces("application/json")
    public Response updateRace(@Context HttpServletRequest request, Race race) {
        LOG.debug("Updating race with id " + race.getId());
        try {
            service.updateRace(race, request.getSession().getId());
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

    @Inject
    // sonar want constructor injection which jaxrs does not support
    public void setService(RaceService service) {
        this.service = service;
    }

    @Inject
    public void setCache(Cache cache) {
        this.cache = cache;
    }
}
