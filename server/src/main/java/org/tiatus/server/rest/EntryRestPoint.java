package org.tiatus.server.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.entity.Entry;
import org.tiatus.role.Role;
import org.tiatus.service.EntryService;
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
@Path("entries")
@SuppressWarnings("squid:S1166")
public class EntryRestPoint {

    private static final Logger LOG = LoggerFactory.getLogger(EntryRestPoint.class);

    private EntryService service;

    /**
     * Get entries
     * @return response containing list of entries
     */
    @PermitAll
    @GET
    @Produces("application/json")
    public Response getEntries() {
        List<Entry> entries = service.getEntries();
        return Response.ok(entries).build();
    }

    /**
     * Add entry, restricted to Admin users
     * @param uriInfo location details
     * @param entry to add
     * @return 201 response with location containing uri of newly created entry or an error code
     */
    @RolesAllowed({Role.ADMIN})
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response addEntry(@Context UriInfo uriInfo, Entry entry) {
        LOG.debug("Adding entry " + entry);
        try {
            Entry saved = service.addEntry(entry);
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
     * Remove entry, restricted to Admin users
     * @param id of entry to remove
     * @return response with 204
     */
    @RolesAllowed({Role.ADMIN})
    @DELETE
    @Path("{id}")
    @Produces("application/json")
    public Response removeEntry(@PathParam("id") String id) {
        LOG.debug("Removing entry with id " + id);
        try {
            Entry entry = new Entry();
            entry.setId(Long.parseLong(id));
            service.deleteEntry(entry);
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
    public void setService(EntryService service) {
        this.service = service;
    }
}
