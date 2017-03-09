package org.tiatus.server.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.entity.RacePositionTemplate;
import org.tiatus.role.Role;
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
    public Response addRace(@Context UriInfo uriInfo, RacePositionTemplate template) {
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
    public Response removeRace(@PathParam("id") String id) {
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
    public Response updateRace(RacePositionTemplate template) {
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

    @Inject
    public void setService(RacePositionTemplateService service) {
        this.service = service;
    }
}
