package org.tiatus.server.rest;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.tiatus.entity.Position;
// import org.tiatus.role.Role;
// import org.tiatus.service.PositionService;

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
// @Path("positions")
// @SuppressWarnings("squid:S1166")
public class PositionRestPoint extends RestBase {

    // private static final Logger LOG = LoggerFactory.getLogger(PositionRestPoint.class);

    // private PositionService service;

    // /**
    //  * Get positions
    //  * @return response containing list of positions
    //  */
    // @PermitAll
    // @GET
    // @Produces("application/json")
    // public Response getPositions(@Context Request request) {
    //     List<Position> positions = service.getPositions();
    //     return Response.ok(positions).build();
    // }

    // /**
    //  * Add position, restricted to Admin users
    //  * @param uriInfo location details
    //  * @param position to add
    //  * @return 201 response with location containing uri of newly created position or an error code
    //  */
    // @RolesAllowed({Role.ADMIN})
    // @POST
    // @Consumes("application/json")
    // @Produces("application/json")
    // public Response addPosition(@Context UriInfo uriInfo, @Context HttpServletRequest request, Position position) {
    //     LOG.debug("Adding position " + position);
    //     try {
    //         Position saved = service.addPosition(position, request.getSession().getId());
    //         return Response.created(URI.create(uriInfo.getPath() + "/"+ saved.getId())).build();

    //     } catch (Exception e) {
    //         return logError(e);
    //     }
    // }

    // /**
    //  * Remove position, restricted to Admin users
    //  * @param id of position to remove
    //  * @return response with 204
    //  */
    // @RolesAllowed({Role.ADMIN})
    // @DELETE
    // @Path("{id}")
    // @Produces("application/json")
    // public Response removePosition(@PathParam("id") Long id, @Context HttpServletRequest request) {
    //     LOG.debug("Removing position with id " + id);
    //     try {
    //         Position position = service.getPositionForId(id);
    //         if (position != null) {
    //             service.removePosition(position, request.getSession().getId());
    //         }
    //         return Response.noContent().build();

    //     } catch (Exception e) {
    //         return logError(e);
    //     }
    // }

    // /**
    //  * Update position, restricted to Admin users
    //  * @param id of position to update
    //  * @param position to update
    //  * @return response with 204
    //  */
    // @RolesAllowed({Role.ADMIN})
    // @PUT
    // @Path("{id}")
    // @Produces("application/json")
    // public Response updatePosition(@PathParam("id") Long id, Position position, @Context HttpServletRequest request) {
    //     LOG.debug("Updating position with id " + id);
    //     try {
    //         Position existing = service.getPositionForId(id);
    //         if (existing == null) {
    //             LOG.warn("Failed to get position for supplied id");
    //             return Response.status(Response.Status.NOT_FOUND).build();
    //         }

    //         service.updatePosition(position, request.getSession().getId());
    //         return Response.noContent().build();

    //     } catch (Exception e) {
    //         return logError(e);
    //     }
    // }

    // @Inject
    // // sonar want constructor injection which jaxrs does not support
    // public void setService(PositionService service) {
    //     this.service = service;
    // }

}
