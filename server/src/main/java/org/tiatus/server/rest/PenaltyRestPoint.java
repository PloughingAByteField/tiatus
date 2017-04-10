package org.tiatus.server.rest;

import org.infinispan.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.entity.Penalty;
import org.tiatus.role.Role;
import org.tiatus.service.PenaltyService;
import org.tiatus.service.ServiceException;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;

/**
 * Created by johnreynolds on 19/06/2016.
 */
@Path("penalties")
@SuppressWarnings("squid:S1166")
public class PenaltyRestPoint {

    private static final Logger LOG = LoggerFactory.getLogger(PenaltyRestPoint.class);
    private static final String CACHE_NAME = "penalties";

    private PenaltyService service;
    private Cache cache;

    /**
     * Get penalties
     * @return response containing list of penalties
     */
    @PermitAll
    @GET
    @Produces("application/json")
    public Response getPenalties(@Context Request request) {
        Response.ResponseBuilder builder;
        if (cache.get(CACHE_NAME) != null) {
            CacheEntry cacheEntry = (CacheEntry)cache.get(CACHE_NAME);
            String cachedEntryETag = cacheEntry.getETag();

            EntityTag cachedRacesETag = new EntityTag(cachedEntryETag, false);
            builder = request.evaluatePreconditions(cachedRacesETag);
            if (builder == null) {
                List<Penalty> penalties = (List<Penalty>)cacheEntry.getEntry();
                builder = Response.ok(penalties).tag(cachedEntryETag);
            }
        } else {
            List<Penalty> penalties = service.getPenalties();
            String hashCode = Integer.toString(penalties.hashCode());
            EntityTag etag = new EntityTag(hashCode, false);
            CacheEntry newCacheEntry = new CacheEntry(hashCode, penalties);
            cache.put(CACHE_NAME, newCacheEntry);
            builder = Response.ok(penalties).tag(etag);
        }

        return builder.build();
    }

    /**
     * Add penalty, restricted to Adjudicator users
     * @param uriInfo location details
     * @param penalty to add
     * @return 201 response with location containing uri of newly created penalty or an error code
     */
    @RolesAllowed({Role.ADJUDICATOR})
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response addPenalty(@Context UriInfo uriInfo, Penalty penalty) {
        LOG.debug("Adding penalty " + penalty);
        try {
            Penalty saved = service.addPenalty(penalty);
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
     * Remove penalty, restricted to Adjudicator users
     * @param id of penalty to remove
     * @return response with 204
     */
    @RolesAllowed({Role.ADJUDICATOR})
    @DELETE
    @Path("{id}")
    @Produces("application/json")
    public Response removePenalty(@PathParam("id") String id) {
        LOG.debug("Removing penalty with id " + id);
        try {
            Penalty penalty = new Penalty();
            penalty.setId(Long.parseLong(id));
            service.deletePenalty(penalty);
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
     * Update penalty, restricted to Adjudicator users
     * @param penalty to update
     * @return 200 response or an error code
     */
    @PUT
    @RolesAllowed({Role.ADJUDICATOR})
    @Produces("application/json")
    public Response updatePenalty(Penalty penalty) {
        LOG.debug("updating penalty");
        try {
            service.updatePenalty(penalty);
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
    public void setService(PenaltyService service) {
        this.service = service;
    }

    @Inject
    public void setCache(Cache cache) {
        this.cache = cache;
    }
}
