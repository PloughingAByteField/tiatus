package org.tiatus.server.rest;

import org.infinispan.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.entity.Position;
import org.tiatus.role.Role;
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
 * Created by johnreynolds on 19/06/2016.
 */
@Path("positions")
@SuppressWarnings("squid:S1166")
public class PositionRestPoint {

    private static final Logger LOG = LoggerFactory.getLogger(PositionRestPoint.class);
    private static final String CACHE_NAME = "positions";

    private PositionService service;
    private Cache cache;

    /**
     * Get positions
     * @return response containing list of positions
     */
    @PermitAll
    @GET
    @Produces("application/json")
    public Response getPositions(@Context Request request) {
        Response.ResponseBuilder builder;
        if (cache.get(CACHE_NAME) != null) {
            CacheEntry cacheEntry = (CacheEntry)cache.get(CACHE_NAME);
            String cachedEntryETag = cacheEntry.getETag();

            EntityTag cachedRacesETag = new EntityTag(cachedEntryETag, false);
            builder = request.evaluatePreconditions(cachedRacesETag);
            if (builder == null) {
                List<Position> positions = (List<Position>)cacheEntry.getEntry();
                builder = Response.ok(positions).tag(cachedEntryETag);
            }
        } else {
            List<Position> positions = service.getPositions();
            String hashCode = Integer.toString(positions.hashCode());
            EntityTag etag = new EntityTag(hashCode, false);
            CacheEntry newCacheEntry = new CacheEntry(hashCode, positions);
            cache.put(CACHE_NAME, newCacheEntry);
            builder = Response.ok(positions).tag(etag);
        }

        return builder.build();
    }

    /**
     * Add position, restricted to Admin users
     * @param uriInfo location details
     * @param position to add
     * @return 201 response with location containing uri of newly created position or an error code
     */
    @RolesAllowed({Role.ADMIN})
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response addPosition(@Context UriInfo uriInfo, @Context HttpServletRequest request, Position position) {
        LOG.debug("Adding position " + position);
        try {
            Position saved = service.addPosition(position, request.getSession().getId());
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
     * Remove position, restricted to Admin users
     * @param id of position to remove
     * @return response with 204
     */
    @RolesAllowed({Role.ADMIN})
    @DELETE
    @Path("{id}")
    @Produces("application/json")
    public Response removePosition(@PathParam("id") String id, @Context HttpServletRequest request) {
        LOG.debug("Removing position with id " + id);
        try {
            Position position = new Position();
            position.setId(Long.parseLong(id));
            service.removePosition(position, request.getSession().getId());
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
     * Update position, restricted to Admin users
     * @param position to update
     * @return response with 204
     */
    @RolesAllowed({Role.ADMIN})
    @PUT
    @Produces("application/json")
    public Response updatePosition(Position position, @Context HttpServletRequest request) {
        LOG.debug("Updating position with id " + position.getId());
        try {
            service.updatePosition(position, request.getSession().getId());
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
    public void setService(PositionService service) {
        this.service = service;
    }

    @Inject
    public void setCache(Cache cache) {
        this.cache = cache;
    }
}
