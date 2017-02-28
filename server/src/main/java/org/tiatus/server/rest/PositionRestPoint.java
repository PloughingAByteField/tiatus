package org.tiatus.server.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.entity.Position;
import org.tiatus.role.Role;
import org.tiatus.service.PositionService;
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
@Path("positions")
@SuppressWarnings("squid:S1166")
public class PositionRestPoint {

    private static final Logger LOG = LoggerFactory.getLogger(PositionRestPoint.class);

    private PositionService service;

    /**
     * Get positions
     * @return response containing list of positions
     */
    @PermitAll
    @GET
    @Produces("application/json")
    public Response getPositions() {
        List<Position> positions = service.getPositions();
        return Response.ok(positions).build();
    }

    @PermitAll
    @GET
    @Path("activeTiming")
    @Produces("application/json")
    public Response getActiveTimingPositions() {
        List<Position> positions = service.getActiveTimingPositions();
        return Response.ok(positions).build();
    }

    /**
     * Add position, restricted to Admin users
     * @param uriInfo location details
     * @param position to add
     * @return 201 response with location containing uri of newly created position or an error code
     */
    @RolesAllowed({Role.ADMIN})
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response addPosition(@Context UriInfo uriInfo, Position position) {
        LOG.debug("Adding position " + position);
        try {
            Position saved = service.addPosition(position);
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
     * Remove position, restricted to Admin users
     * @param id of position to remove
     * @return response with 204
     */
    @RolesAllowed({Role.ADMIN})
    @DELETE
    @Path("{id}")
    @Produces("application/json")
    public Response removePosition(@PathParam("id") String id) {
        LOG.debug("Removing position with id " + id);
        try {
            Position position = new Position();
            position.setId(Long.parseLong(id));
            service.removePosition(position);
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
     * Update position, restricted to Admin users
     * @param position to update
     * @return response with 204
     */
    @RolesAllowed({Role.ADMIN})
    @PUT
    @Produces("application/json")
    public Response updatePosition(Position position) {
        LOG.debug("Updating position with id " + position.getId());
        try {
            service.updatePosition(position);
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
    // sonar want constructor injection which jaxrs does not support
    public void setService(PositionService service) {
        this.service = service;
    }
}
