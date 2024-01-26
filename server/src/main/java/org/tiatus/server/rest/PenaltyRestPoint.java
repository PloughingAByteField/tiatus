package org.tiatus.server.rest;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.tiatus.entity.Penalty;
// import org.tiatus.role.Role;
// import org.tiatus.service.PenaltyService;

// import javax.annotation.security.PermitAll;
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
// @Path("penalties")
// @SuppressWarnings("squid:S1166")
public class PenaltyRestPoint extends RestBase {

    // private static final Logger LOG = LoggerFactory.getLogger(PenaltyRestPoint.class);

    // private PenaltyService service;

    // /**
    //  * Get penalties
    //  * @return response containing list of penalties
    //  */
    // @PermitAll
    // @GET
    // @Produces("application/json")
    // public Response getPenalties(@Context Request request) {
    //     List<Penalty> penalties = service.getPenalties();
    //     return Response.ok(penalties).build();
    // }

    // /**
    //  * Add penalty, restricted to Adjudicator users
    //  * @param uriInfo location details
    //  * @param penalty to add
    //  * @return 201 response with location containing uri of newly created penalty or an error code
    //  */
    // @RolesAllowed({Role.ADJUDICATOR})
    // @POST
    // @Consumes("application/json")
    // @Produces("application/json")
    // public Response addPenalty(@Context UriInfo uriInfo, @Context HttpServletRequest request, Penalty penalty) {
    //     LOG.debug("Adding penalty " + penalty);
    //     try {
    //         Penalty saved = service.addPenalty(penalty, request.getSession().getId());
    //         return Response.created(URI.create(uriInfo.getPath() + "/"+ saved.getId())).build();

    //     } catch (Exception e) {
    //         return logError(e);
    //     }
    // }

    // /**
    //  * Remove penalty, restricted to Adjudicator users
    //  * @param id of penalty to remove
    //  * @return response with 204
    //  */
    // @RolesAllowed({Role.ADJUDICATOR})
    // @DELETE
    // @Path("{id}")
    // @Produces("application/json")
    // public Response removePenalty(@PathParam("id") Long id, @Context HttpServletRequest request) {
    //     LOG.debug("Removing penalty with id " + id);
    //     try {
    //         Penalty penalty = service.getPenaltyForId(id);
    //         if (penalty != null) {
    //             service.deletePenalty(penalty, request.getSession().getId());
    //         }
    //         return Response.noContent().build();

    //     } catch (Exception e) {
    //         return logError(e);
    //     }
    // }

    // /**
    //  * Update penalty, restricted to Adjudicator users
    //  * @param id of penalty to update
    //  * @param penalty to update
    //  * @return 200 response or an error code
    //  */
    // @PUT
    // @Path("{id}")
    // @RolesAllowed({Role.ADJUDICATOR})
    // @Produces("application/json")
    // public Response updatePenalty(@PathParam("id") Long id, Penalty penalty, @Context HttpServletRequest request) {
    //     LOG.debug("updating penalty");
    //     try {
    //         Penalty existing = service.getPenaltyForId(id);
    //         if (existing == null) {
    //             LOG.warn("Failed to get penalty for supplied id");
    //             return Response.status(Response.Status.NOT_FOUND).build();
    //         }

    //         service.updatePenalty(penalty, request.getSession().getId());
    //         return Response.noContent().build();

    //     } catch (Exception e) {
    //         return logError(e);
    //     }
    // }

    // @Inject
    // public void setService(PenaltyService service) {
    //     this.service = service;
    // }

}
