package org.tiatus.server.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.entity.User;
import org.tiatus.service.ServiceException;
import org.tiatus.service.UserService;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Map;

import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

/**
 * Created by johnreynolds on 04/09/2016.
 */
@Path("setup")
@SuppressWarnings("squid:S1166")
public class SetupRestPoint {

    private static final Logger LOG = LoggerFactory.getLogger(SetupRestPoint.class);

    private UserService service;

    /**
     * Add an admin user via setup page
     * @param uriInfo location details
     * @param user user to add
     * @return response contain success or failure code
     */
    @PermitAll
    @POST
    @Path("user")
    @Consumes("application/json")
    @Produces("application/json")
    public Response addUser(@Context UriInfo uriInfo, User user) {
        checkIfSetupHasAlreadyRun();
        LOG.debug("Adding admin user " + user);
        try {
            service.addAdminUser(user);
            return Response.created(URI.create(uriInfo.getPath() + "/"+ user.getId())).entity(user).build();

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
    public void setService(UserService service) {
        this.service = service;
    }

    private void checkIfSetupHasAlreadyRun() {
        if (service.hasAdminUser()) {
            LOG.warn("Already have run setup");
            throw new InternalServerErrorException();
        }
    }
}
