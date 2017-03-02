package org.tiatus.server.rest;

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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

/**
 * Created by johnreynolds on 19/06/2016.
 */
@Path("penalties")
@SuppressWarnings("squid:S1166")
public class PenaltyRestPoint {

    private static final Logger LOG = LoggerFactory.getLogger(PenaltyRestPoint.class);

    private PenaltyService service;

    /**
     * Get penalties
     * @return response containing list of penalties
     */
    @PermitAll
    @GET
    @Produces("application/json")
    public Response getPenalties() {
        List<Penalty> penalties = service.getPenalties();
        return Response.ok(penalties).build();
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
}
