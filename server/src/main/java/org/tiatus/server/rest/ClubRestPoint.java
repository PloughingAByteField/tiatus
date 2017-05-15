package org.tiatus.server.rest;

import org.infinispan.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.entity.Club;
import org.tiatus.role.Role;
import org.tiatus.service.ClubService;
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
@Path("clubs")
@SuppressWarnings("squid:S1166")
public class ClubRestPoint extends RestBase {

    private static final Logger LOG = LoggerFactory.getLogger(ClubRestPoint.class);

    private static final String CACHE_NAME = "clubs";
    private ClubService service;
    private Cache cache;

    /**
     * Get clubs
     * @return response containing list of clubs
     */
    @PermitAll
    @GET
    @Produces("application/json")
    public Response getClubs(@Context Request request) {
        Response.ResponseBuilder builder;
        if (cache.get(CACHE_NAME) != null) {
            CacheEntry cacheEntry = (CacheEntry)cache.get(CACHE_NAME);
            String cachedEntryETag = cacheEntry.getETag();

            EntityTag cachedRacesETag = new EntityTag(cachedEntryETag, false);
            builder = request.evaluatePreconditions(cachedRacesETag);
            if (builder == null) {
                List<Club> clubs = (List<Club>)cacheEntry.getEntry();
                builder = Response.ok(clubs).tag(cachedEntryETag);
            }
        } else {
            List<Club> clubs = service.getClubs();
            String hashCode = Integer.toString(clubs.hashCode());
            EntityTag etag = new EntityTag(hashCode, false);
            CacheEntry newCacheEntry = new CacheEntry(hashCode, clubs);
            cache.put(CACHE_NAME, newCacheEntry);
            builder = Response.ok(clubs).tag(etag);
        }

        return builder.build();
    }

    /**
     * Add club, restricted to Admin users
     * @param uriInfo location details
     * @param club to add
     * @return 201 response with location containing uri of newly created club or an error code
     */
    @RolesAllowed({Role.ADMIN})
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response addClub(@Context UriInfo uriInfo, @Context HttpServletRequest request, Club club) {
        LOG.debug("Adding club " + club);
        try {
            Club saved = service.addClub(club, request.getSession().getId());
            if (cache.get(CACHE_NAME) != null) {
                cache.evict(CACHE_NAME);
            }
            return Response.created(URI.create(uriInfo.getPath() + "/"+ saved.getId())).build();

        } catch (Exception e) {
            return logError(e);
        }
    }

    /**
     * Remove club, restricted to Admin users
     * @param id of club to remove
     * @return response with 204
     */
    @RolesAllowed({Role.ADMIN})
    @DELETE
    @Path("{id}")
    @Produces("application/json")
    public Response removeClub(@PathParam("id") String id, @Context HttpServletRequest request) {
        LOG.debug("Removing club with id " + id);
        try {
            Club club = service.getClubForId(Long.parseLong(id));;
            if (club != null) {
                service.deleteClub(club, request.getSession().getId());
                if (cache.get(CACHE_NAME) != null) {
                    cache.evict(CACHE_NAME);
                }
            }
            return Response.noContent().build();

        } catch (Exception e) {
            return logError(e);
        }
    }

    /**
     * Update club, restricted to Admin users
     * @param id of club to update
     * @param club to update
     * @return 200 response or an error code
     */
    @PUT
    @Path("{id}")
    @RolesAllowed({Role.ADMIN})
    @Produces("application/json")
    public Response updateClub(@PathParam("id") String id, @Context HttpServletRequest request, Club club) {
        LOG.debug("updating club of id " + id);
        try {
            Club existing = service.getClubForId(Long.parseLong(id));
            if (existing == null) {
                LOG.warn("Failed to get club for supplied id");
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            service.updateClub(club, request.getSession().getId());
            if (cache.get(CACHE_NAME) != null) {
                cache.evict(CACHE_NAME);
            }
            return Response.noContent().build();

        } catch (Exception e) {
            return logError(e);
        }
    }

    @Inject
    public void setService(ClubService service) {
        this.service = service;
    }

    @Inject
    public void setCache(Cache cache) {
        this.cache = cache;
    }
}
