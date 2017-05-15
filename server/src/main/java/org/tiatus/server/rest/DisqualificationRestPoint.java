package org.tiatus.server.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.entity.Disqualification;
import org.tiatus.role.Role;
import org.tiatus.service.DisqualificationService;

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
public class DisqualificationRestPoint extends RestBase {

    private static final Logger LOG = LoggerFactory.getLogger(DisqualificationRestPoint.class);

    private DisqualificationService service;

    /**
     * Get disqualifications
     * @return response containing list of disqualifications
     */
    @PermitAll
    @GET
    @Produces("application/json")
    public Response getDisqualifications(@Context Request request) {
        List<Disqualification> disqualifications = service.getDisqualifications();
        return Response.ok(disqualifications).build();
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
            return Response.created(URI.create(uriInfo.getPath() + "/"+ saved.getId())).build();

        } catch (Exception e) {
            return logError(e);
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
            Disqualification disqualification = service.getDisqualificationForId(Long.parseLong(id));
            if (disqualification != null) {
                service.deleteDisqualification(disqualification, request.getSession().getId());
            }
            return Response.noContent().build();

        } catch (Exception e) {
            return logError(e);
        }
    }

    /**
     * Update disqualification, restricted to Adjudicator users
     * @param id of disqualification to update
     * @param disqualification to update
     * @return 200 response or an error code
     */
    @PUT
    @Path("{id}")
    @RolesAllowed({Role.ADJUDICATOR})
    @Produces("application/json")
    public Response updateDisqualification(@PathParam("id") String id, @Context HttpServletRequest request, Disqualification disqualification) {
        LOG.debug("updating disqualification");
        try {
            Disqualification existing = service.getDisqualificationForId(Long.parseLong(id));
            if (existing == null) {
                LOG.warn("Failed to get disqualification for supplied id");
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            service.updateDisqualification(disqualification, request.getSession().getId());
            return Response.noContent().build();

        } catch (Exception e) {
            return logError(e);
        }
    }

    @Inject
    public void setService(DisqualificationService service) {
        this.service = service;
    }

}
