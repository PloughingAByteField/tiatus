package org.tiatus.server.rest;

import org.infinispan.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.entity.Disqualification;
import org.tiatus.role.Role;
import org.tiatus.service.DisqualificationService;
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
@Path("disqualifications")
@SuppressWarnings("squid:S1166")
public class DisqualificationRestPoint {

    private static final Logger LOG = LoggerFactory.getLogger(DisqualificationRestPoint.class);
    private static final String CACHE_NAME = "disqualifications";

    private DisqualificationService service;
    private Cache cache;

    /**
     * Get disqualifications
     * @return response containing list of disqualifications
     */
    @PermitAll
    @GET
    @Produces("application/json")
    public Response getDisqualifications(@Context Request request) {
        Response.ResponseBuilder builder;
        if (cache.get(CACHE_NAME) != null) {
            CacheEntry cacheEntry = (CacheEntry)cache.get(CACHE_NAME);
            String cachedEntryETag = cacheEntry.getETag();

            EntityTag cachedRacesETag = new EntityTag(cachedEntryETag, false);
            builder = request.evaluatePreconditions(cachedRacesETag);
            if (builder == null) {
                List<Disqualification> disqualifications = (List<Disqualification>)cacheEntry.getEntry();
                builder = Response.ok(disqualifications).tag(cachedEntryETag);
            }
        } else {
            List<Disqualification> disqualifications = service.getDisqualifications();
            String hashCode = Integer.toString(disqualifications.hashCode());
            EntityTag etag = new EntityTag(hashCode, false);
            CacheEntry newCacheEntry = new CacheEntry(hashCode, disqualifications);
            cache.put(CACHE_NAME, newCacheEntry);
            builder = Response.ok(disqualifications).tag(etag);
        }

        return builder.build();
    }

    /**
     * Add disqualification, restricted to Adjudicator users
     * @param uriInfo location details
     * @param disqualification to add
     * @return 201 response with location containing uri of newly created disqualification or an error code
     */
    @RolesAllowed({Role.ADJUDICATOR})
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response addDisqualification(@Context UriInfo uriInfo, @Context HttpServletRequest request, Disqualification disqualification) {
        LOG.debug("Adding disqualification " + disqualification);
        try {
            Disqualification saved = service.addDisqualification(disqualification, request.getSession().getId());
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
     * Remove disqualification, restricted to Adjudicator users
     * @param id of disqualification to remove
     * @return response with 204
     */
    @RolesAllowed({Role.ADJUDICATOR})
    @DELETE
    @Path("{id}")
    @Produces("application/json")
    public Response removeDisqualification(@PathParam("id") String id, @Context HttpServletRequest request) {
        LOG.debug("Removing disqualification with id " + id);
        try {
            Disqualification disqualification = new Disqualification();
            disqualification.setId(Long.parseLong(id));
            service.deleteDisqualification(disqualification, request.getSession().getId());
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
     * Update disqualification, restricted to Adjudicator users
     * @param disqualification to update
     * @return 200 response or an error code
     */
    @PUT
    @RolesAllowed({Role.ADJUDICATOR})
    @Produces("application/json")
    public Response updateDisqualification(@Context HttpServletRequest request, Disqualification disqualification) {
        LOG.debug("updating disqualification");
        try {
            service.updateDisqualification(disqualification, request.getSession().getId());
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
    public void setService(DisqualificationService service) {
        this.service = service;
    }

    @Inject
    public void setCache(Cache cache) {
        this.cache = cache;
    }
}
