package org.tiatus.server.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.entity.Disqualification;
import org.tiatus.role.Role;
import org.tiatus.service.DisqualificationService;
import org.tiatus.service.ServiceException;

import javax.annotation.security.PermitAll;
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
@Path("disqualifications")
@SuppressWarnings("squid:S1166")
public class DisqualificationRestPoint {

    private static final Logger LOG = LoggerFactory.getLogger(DisqualificationRestPoint.class);

    private DisqualificationService service;

    /**
     * Get disqualifications
     * @return response containing list of disqualifications
     */
    @PermitAll
    @GET
    @Produces("application/json")
    public Response getDisqualifications() {
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
    public Response addDisqualification(@Context UriInfo uriInfo, Disqualification disqualification) {
        LOG.debug("Adding disqualification " + disqualification);
        try {
            Disqualification saved = service.addDisqualification(disqualification);
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
    public Response removeDisqualification(@PathParam("id") String id) {
        LOG.debug("Removing disqualification with id " + id);
        try {
            Disqualification disqualification = new Disqualification();
            disqualification.setId(Long.parseLong(id));
            service.deleteDisqualification(disqualification);
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
    public Response updateDisqualification(Disqualification disqualification) {
        LOG.debug("updating disqualification");
        try {
            service.updateDisqualification(disqualification);

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
}
