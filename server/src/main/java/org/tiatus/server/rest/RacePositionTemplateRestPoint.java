package org.tiatus.server.rest;

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
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

/**
 * Created by johnreynolds on 19/06/2016.
 */
@Path("racePositionTemplates")
@SuppressWarnings("squid:S1166")
public class RacePositionTemplateRestPoint {

    private static final Logger LOG = LoggerFactory.getLogger(RacePositionTemplateRestPoint.class);

    private RacePositionTemplateService service;
    private PositionService positionService;

    /**
     * Get templates restricted to Admin users
     * @return response containing list of templates
     */
    @RolesAllowed({Role.ADMIN})
    @GET
    @Produces("application/json")
    public Response getTemplates() {
        List<RacePositionTemplate> templates = service.getRacePositionTemplates();
        return Response.ok(templates).build();
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
    public Response addTemplate(@Context UriInfo uriInfo, RacePositionTemplate template) {
        LOG.debug("Adding template " + template.getName());
        try {
            RacePositionTemplate saved = service.addRacePositionTemplate(template);
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
    public Response deleteTemplate(@PathParam("id") String id) {
        LOG.debug("Removing template with id " + id);
        try {
            RacePositionTemplate template = new RacePositionTemplate();
            template.setId(Long.parseLong(id));
            service.deleteRacePositionTemplate(template);
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
    public Response updateTemplate(RacePositionTemplate template) {
        LOG.debug("Updating template with id " + template.getId());
        try {
            service.updateRacePositionTemplate(template);
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
    public Response addTemplateEntry(@Context UriInfo uriInfo, RacePositionTemplateEntry entry) {
        LOG.debug("Adding template " + entry.getTemplate().getId() + " at position " + entry.getPosition().getId());
        try {
            Position position = positionService.getPositionForId(entry.getPositionId());
            entry.setPosition(position);
            RacePositionTemplate template = service.getTemplateForId(entry.getTemplateId());
            entry.setTemplate(template);
            RacePositionTemplateEntry saved = service.addTemplateEntry(entry);
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
    public Response deleteTemplateEntry(@PathParam("templateId") String templateId, @PathParam("positionId") String positionId) {
        LOG.debug("Removing template with templateId " + templateId + " and positionId " + positionId);
        try {
            RacePositionTemplateEntry entry = new RacePositionTemplateEntry();
            Position position = positionService.getPositionForId(Long.parseLong(positionId));
            entry.setPosition(position);
            RacePositionTemplate template = service.getTemplateForId(Long.parseLong(templateId));
            entry.setTemplate(template);
            service.deleteTemplateEntry(entry);
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
    public Response updateTemplateEntry(RacePositionTemplateEntry entry) {
        LOG.debug("Updating template entry with id " + entry.getTemplate().getId());
        try {
            Position position = positionService.getPositionForId(entry.getPositionId());
            entry.setPosition(position);
            RacePositionTemplate template = service.getTemplateForId(entry.getTemplateId());
            entry.setTemplate(template);
            service.updateTemplateEntry(entry);
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
}
