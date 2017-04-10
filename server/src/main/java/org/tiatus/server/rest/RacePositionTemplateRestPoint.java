package org.tiatus.server.rest;

import org.infinispan.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.entity.Position;
import org.tiatus.entity.RacePositionTemplate;
import org.tiatus.entity.RacePositionTemplateEntry;
import org.tiatus.role.Role;
import org.tiatus.service.PositionService;
import org.tiatus.service.RacePositionTemplateService;
import org.tiatus.service.ServiceException;

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
@Path("racePositionTemplates")
@SuppressWarnings("squid:S1166")
public class RacePositionTemplateRestPoint {

    private static final Logger LOG = LoggerFactory.getLogger(RacePositionTemplateRestPoint.class);
    private static final String CACHE_NAME = "racePositionTemplates";

    private RacePositionTemplateService service;
    private PositionService positionService;
    private Cache cache;

    /**
     * Get templates restricted to Admin users
     * @return response containing list of templates
     */
    @RolesAllowed({Role.ADMIN})
    @GET
    @Produces("application/json")
    public Response getTemplates(@Context Request request) {
        Response.ResponseBuilder builder;
        if (cache.get(CACHE_NAME) != null) {
            CacheEntry cacheEntry = (CacheEntry)cache.get(CACHE_NAME);
            String cachedEntryETag = cacheEntry.getETag();

            EntityTag cachedRacesETag = new EntityTag(cachedEntryETag, false);
            builder = request.evaluatePreconditions(cachedRacesETag);
            if (builder == null) {
                List<RacePositionTemplate> templates = (List<RacePositionTemplate>)cacheEntry.getEntry();
                builder = Response.ok(templates).tag(cachedEntryETag);
            }
        } else {
            List<RacePositionTemplate> templates = service.getRacePositionTemplates();
            String hashCode = Integer.toString(templates.hashCode());
            EntityTag etag = new EntityTag(hashCode, false);
            CacheEntry newCacheEntry = new CacheEntry(hashCode, templates);
            cache.put(CACHE_NAME, newCacheEntry);
            builder = Response.ok(templates).tag(etag);
        }

        return builder.build();
    }

    /**
     * Add template, restricted to Admin users
     * @param uriInfo location details
     * @param template to add
     * @return 201 response with location containing uri of newly created template or an error code
     */
    @RolesAllowed({Role.ADMIN})
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response addTemplate(@Context UriInfo uriInfo, RacePositionTemplate template, @Context HttpServletRequest request) {
        LOG.debug("Adding template " + template.getName());
        try {
            RacePositionTemplate saved = service.addRacePositionTemplate(template, request.getSession().getId());
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
     * Remove template, restricted to Admin users
     * @param id of template to remove
     * @return response with 204
     */
    @RolesAllowed({Role.ADMIN})
    @DELETE
    @Path("{id}")
    @Produces("application/json")
    public Response deleteTemplate(@PathParam("id") String id, @Context HttpServletRequest request) {
        LOG.debug("Removing template with id " + id);
        try {
            RacePositionTemplate template = service.getTemplateForId(Long.parseLong(id));
            service.deleteRacePositionTemplate(template, request.getSession().getId());
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
     * Update template, restricted to  Admin users
     * @param template of template to update
     * @return response with 204
     */
    @RolesAllowed({Role.ADMIN})
    @PUT
    @Produces("application/json")
    public Response updateTemplate(RacePositionTemplate template, @Context HttpServletRequest request) {
        LOG.debug("Updating template with id " + template.getId());
        try {
            RacePositionTemplate templateToUpdate = service.getTemplateForId(template.getId());
            templateToUpdate.setName(template.getName());
            templateToUpdate.setDefaultTemplate(template.getDefaultTemplate());
            service.updateRacePositionTemplate(templateToUpdate, request.getSession().getId());
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
     * Add entry to template, restricted to Admin users
     * @param uriInfo location details
     * @param entry to add
     * @return 201 response with location containing uri of newly created template or an error code
     */
    @RolesAllowed({Role.ADMIN})
    @POST
    @Path("entry")
    @Consumes("application/json")
    @Produces("application/json")
    public Response addTemplateEntry(@Context UriInfo uriInfo, RacePositionTemplateEntry entry, @Context HttpServletRequest request) {
        LOG.debug("Adding template " + entry.getTemplate().getId() + " at position " + entry.getPosition().getId());
        try {
            Position position = positionService.getPositionForId(entry.getPositionId());
            entry.setPosition(position);
            RacePositionTemplate template = service.getTemplateForId(entry.getTemplateId());
            entry.setTemplate(template);
            RacePositionTemplateEntry saved = service.addTemplateEntry(entry, request.getSession().getId());
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
     * Remove entry from template, restricted to Admin users
     * @param templateId id of template in entry
     * @param positionId id of position in entry
     * @return response with 204
     */
    @RolesAllowed({Role.ADMIN})
    @DELETE
    @Path("entry/template/{templateId}/position/{positionId}")
    @Produces("application/json")
    public Response deleteTemplateEntry(@PathParam("templateId") String templateId, @PathParam("positionId") String positionId, @Context HttpServletRequest request) {
        LOG.debug("Removing template with templateId " + templateId + " and positionId " + positionId);
        try {
            RacePositionTemplateEntry entry = new RacePositionTemplateEntry();
            Position position = positionService.getPositionForId(Long.parseLong(positionId));
            entry.setPosition(position);
            RacePositionTemplate template = service.getTemplateForId(Long.parseLong(templateId));
            entry.setTemplate(template);
            service.deleteTemplateEntry(entry, request.getSession().getId());
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
     * Update entry of a template, restricted to Admin users
     * @param entry entry to update
     * @return response with 204
     */
    @RolesAllowed({Role.ADMIN})
    @PUT
    @Path("entry")
    @Produces("application/json")
    public Response updateTemplateEntry(RacePositionTemplateEntry entry, @Context HttpServletRequest request) {
        LOG.debug("Updating template entry with id " + entry.getTemplate().getId());
        try {
            Position position = positionService.getPositionForId(entry.getPositionId());
            entry.setPosition(position);
            RacePositionTemplate template = service.getTemplateForId(entry.getTemplateId());
            entry.setTemplate(template);
            service.updateTemplateEntry(entry, request.getSession().getId());
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
    public void setService(RacePositionTemplateService service) {
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
