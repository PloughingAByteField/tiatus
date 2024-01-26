package org.tiatus.server.rest;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.tiatus.entity.Position;
// import org.tiatus.entity.RacePositionTemplate;
// import org.tiatus.entity.RacePositionTemplateEntry;
// import org.tiatus.role.Role;
// import org.tiatus.service.PositionService;
// import org.tiatus.service.RacePositionTemplateService;

// import javax.annotation.security.RolesAllowed;
// import javax.inject.Inject;
// import javax.servlet.http.HttpServletRequest;
// import javax.ws.rs.*;
// import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;

/**
 * Created by johnreynolds on 19/06/2016.
 */
// @Path("racePositionTemplates")
// @SuppressWarnings("squid:S1166")
public class RacePositionTemplateRestPoint extends RestBase {

    // private static final Logger LOG = LoggerFactory.getLogger(RacePositionTemplateRestPoint.class);

    // private RacePositionTemplateService service;
    // private PositionService positionService;

    // /**
    //  * Get templates restricted to Admin users
    //  * @return response containing list of templates
    //  */
    // @RolesAllowed({Role.ADMIN})
    // @GET
    // @Produces("application/json")
    // public Response getTemplates(@Context Request request) {
    //     List<RacePositionTemplate> templates = service.getRacePositionTemplates();
    //     return Response.ok(templates).build();
    // }

    // /**
    //  * Add template, restricted to Admin users
    //  * @param uriInfo location details
    //  * @param template to add
    //  * @return 201 response with location containing uri of newly created template or an error code
    //  */
    // @RolesAllowed({Role.ADMIN})
    // @POST
    // @Consumes("application/json")
    // @Produces("application/json")
    // public Response addTemplate(@Context UriInfo uriInfo, RacePositionTemplate template, @Context HttpServletRequest request) {
    //     LOG.debug("Adding template " + template.getName());
    //     try {
    //         RacePositionTemplate saved = service.addRacePositionTemplate(template, request.getSession().getId());
    //         return Response.created(URI.create(uriInfo.getPath() + "/"+ saved.getId())).entity(saved).build();

    //     } catch (Exception e) {
    //         return logError(e);
    //     }
    // }

    // /**
    //  * Remove template, restricted to Admin users
    //  * @param id of template to remove
    //  * @return response with 204
    //  */
    // @RolesAllowed({Role.ADMIN})
    // @DELETE
    // @Path("{id}")
    // @Produces("application/json")
    // public Response deleteTemplate(@PathParam("id") Long id, @Context HttpServletRequest request) {
    //     LOG.debug("Removing template with id " + id);
    //     try {
    //         RacePositionTemplate template = service.getTemplateForId(id);
    //         if (template != null) {
    //             service.deleteRacePositionTemplate(template, request.getSession().getId());
    //         }
    //         return Response.noContent().build();

    //     } catch (Exception e) {
    //         return logError(e);
    //     }
    // }

    // /**
    //  * Update template, restricted to  Admin users
    //  * @param id of template to update
    //  * @param template of template to update
    //  * @return response with 204
    //  */
    // @RolesAllowed({Role.ADMIN})
    // @PUT
    // @Path("{id}")
    // @Produces("application/json")
    // public Response updateTemplate(@PathParam("id") Long id, RacePositionTemplate template, @Context HttpServletRequest request) {
    //     LOG.debug("Updating template with id " + id);
    //     try {
    //         RacePositionTemplate templateToUpdate = service.getTemplateForId(id);
    //         if (template == null) {
    //             LOG.warn("Failed to get template for supplied id");
    //             return Response.status(Response.Status.NOT_FOUND).build();
    //         }

    //         templateToUpdate.setName(template.getName());
    //         templateToUpdate.setDefaultTemplate(template.getDefaultTemplate());
    //         service.updateRacePositionTemplate(templateToUpdate, request.getSession().getId());
    //         return Response.noContent().build();

    //     } catch (Exception e) {
    //         return logError(e);
    //     }
    // }

    // /**
    //  * Add entry to template, restricted to Admin users
    //  * @param uriInfo location details
    //  * @param entry to add
    //  * @return 201 response with location containing uri of newly created template or an error code
    //  */
    // @RolesAllowed({Role.ADMIN})
    // @POST
    // @Path("entry")
    // @Consumes("application/json")
    // @Produces("application/json")
    // public Response addTemplateEntry(@Context UriInfo uriInfo, RacePositionTemplateEntry entry, @Context HttpServletRequest request) {
    //     LOG.debug("Adding template " + entry.getTemplate().getId() + " at position " + entry.getPosition().getId());
    //     try {
    //         Position position = positionService.getPositionForId(entry.getPositionId());
    //         entry.setPosition(position);
    //         RacePositionTemplate template = service.getTemplateForId(entry.getTemplateId());
    //         entry.setTemplate(template);
    //         RacePositionTemplateEntry saved = service.addTemplateEntry(entry, request.getSession().getId());
    //         return Response.created(URI.create(uriInfo.getPath() + "/"+ saved.getId())).entity(saved).build();

    //     } catch (Exception e) {
    //         return logError(e);
    //     }
    // }

    // /**
    //  * Remove entry from template, restricted to Admin users
    //  * @param templateId id of template in entry
    //  * @param positionId id of position in entry
    //  * @return response with 204
    //  */
    // @RolesAllowed({Role.ADMIN})
    // @DELETE
    // @Path("entry/template/{templateId}/position/{positionId}")
    // @Produces("application/json")
    // public Response deleteTemplateEntry(@PathParam("templateId") Long templateId, @PathParam("positionId") Long positionId, @Context HttpServletRequest request) {
    //     LOG.debug("Removing template with templateId " + templateId + " and positionId " + positionId);
    //     try {
    //         RacePositionTemplateEntry entry = new RacePositionTemplateEntry();
    //         Position position = positionService.getPositionForId(positionId);
    //         entry.setPosition(position);
    //         RacePositionTemplate template = service.getTemplateForId(templateId);
    //         entry.setTemplate(template);
    //         if (position != null && template != null) {
    //             service.deleteTemplateEntry(entry, request.getSession().getId());
    //         }
    //         return Response.noContent().build();

    //     } catch (Exception e) {
    //         return logError(e);
    //     }
    // }

    // /**
    //  * Update entry of a template, restricted to Admin users
    //  * @param entry entry to update
    //  * @return response with 204
    //  */
    // @RolesAllowed({Role.ADMIN})
    // @PUT
    // @Path("entry")
    // @Produces("application/json")
    // public Response updateTemplateEntry(RacePositionTemplateEntry entry, @Context HttpServletRequest request) {
    //     LOG.debug("Updating template entry with id " + entry.getTemplate().getId());
    //     try {
    //         Position position = positionService.getPositionForId(entry.getPositionId());
    //         entry.setPosition(position);
    //         RacePositionTemplate template = service.getTemplateForId(entry.getTemplateId());
    //         entry.setTemplate(template);
    //         service.updateTemplateEntry(entry, request.getSession().getId());
    //         return Response.noContent().build();

    //     } catch (Exception e) {
    //         return logError(e);
    //     }
    // }


    // @Inject
    // public void setService(RacePositionTemplateService service) {
    //     this.service = service;
    // }

    // @Inject
    // public void setPositionService(PositionService service) {
    //     this.positionService = service;
    // }

}
