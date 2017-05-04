package org.tiatus.server.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.entity.User;
import org.tiatus.service.ConfigService;
import org.tiatus.service.ServiceException;
import org.tiatus.service.UserService;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.InputStream;
import java.net.URI;

/**
 * Created by johnreynolds on 04/09/2016.
 */
@Path("setup")
@SuppressWarnings("squid:S1166")
public class SetupRestPoint {

    private static final Logger LOG = LoggerFactory.getLogger(SetupRestPoint.class);

    private ConfigService configService;
    private UserService userService;

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
    public Response addUser(@Context UriInfo uriInfo, @Context HttpServletRequest request, @Context ServletContext context, User user) {
        checkIfSetupHasAlreadyRun();
        LOG.debug("Adding admin user " + user);
        try {
            userService.addAdminUser(user, request.getSession().getId());
            // create default entries in config file
            configService.setEventTitle("Tiatus Timing System");
            InputStream logoStream = getDefaultLogoFile(context);
            if (logoStream != null) {
                configService.setEventLogo(logoStream, "tiatus.svg");
            } else {
                LOG.warn("FAILED to find image");
            }
            return Response.created(URI.create(uriInfo.getPath() + "/"+ user.getId())).entity(user).build();

        } catch (ServiceException e) {
            LOG.warn("Got service exception: ", e.getSuppliedException());
            throw new InternalServerErrorException();

        } catch (Exception e) {
            LOG.warn("Got general exception ", e);
            throw new InternalServerErrorException();
        }
    }

    private InputStream getDefaultLogoFile(ServletContext context) {
        return context.getResourceAsStream("/assets/img/stopwatch.svg");
    }

    @Inject
    // sonar want constructor injection which jaxrs does not support
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Inject
    // sonar want constructor injection which jaxrs does not support
    public void setConfigService(ConfigService configService) {
        this.configService = configService;
    }

    private void checkIfSetupHasAlreadyRun() {
        if (userService.hasAdminUser()) {
            LOG.warn("Already have run setup");
            throw new InternalServerErrorException();
        }
    }
}
