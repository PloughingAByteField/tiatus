package org.tiatus.server.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.auth.UserPrincipal;
import org.tiatus.role.Role;
import org.tiatus.server.filter.LoggedInFilter;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

/**
 * Created by johnreynolds on 27/08/2016.
 */
@Path("login")
public class LoginRestPoint extends RestBase {

    private static final Logger LOG = LoggerFactory.getLogger(RaceRestPoint.class);

    /**
     * Check to see if user has been logged in and redirect to the correct location
     * @param uriInfo details on location
     * @param httpServletRequest servlet request
     * @param securityContext security context
     * @return response to correct location or to login page
     */
    @PermitAll
    @POST
    public Response login(@Context UriInfo uriInfo, @Context HttpServletRequest httpServletRequest, @Context SecurityContext securityContext) {
        try {
            String p = httpServletRequest.getContextPath();
            if (p.isEmpty()) {
                p = "/";
            }
            URI base = uriInfo.getBaseUri();
            HttpSession session = httpServletRequest.getSession();
            if (securityContext.getUserPrincipal() == null) {
                LOG.warn("not logged in");
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
            UserPrincipal principal = (UserPrincipal)securityContext.getUserPrincipal();
            session.setAttribute("userId", principal.getName());
            session.setAttribute("principal", principal);

            // get user type and figure where to redirect to
            String page = "timing";
            if (securityContext.isUserInRole(Role.ADMIN)) {
                page = "admin";
            } else if (securityContext.isUserInRole(Role.ADJUDICATOR)) {
                page = "adjudicator";
            }

            URI redirect = new URI(base.getScheme(), null, base.getHost(), base.getPort(), p + page, null, null);
            LOG.debug("Redirecting to " + redirect);
            return Response.accepted().location(redirect).build();

        } catch (Exception e) {
            return logError(e);
        }
    }
}
