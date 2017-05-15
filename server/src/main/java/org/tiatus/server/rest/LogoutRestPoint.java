package org.tiatus.server.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.server.filter.LoggedInFilter;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

/**
 * Created by johnreynolds on 27/08/2016.
 */
@Path("logout")
public class LogoutRestPoint extends RestBase {

    private static final Logger LOG = LoggerFactory.getLogger(RaceRestPoint.class);

    /**
     * Logout and redirect a user back to login page
     * @param uriInfo location infor
     * @param httpServletRequest servlet request
     * @return user to login page
     */
    @PermitAll
    @GET
    public Response logout(@Context UriInfo uriInfo, @Context HttpServletRequest httpServletRequest) {
        try {
            String p = httpServletRequest.getContextPath();
            URI base = uriInfo.getBaseUri();
            HttpSession session = httpServletRequest.getSession();
            if (session != null) {
                session.invalidate();
            }

            URI redirect = new URI(base.getScheme(), null, base.getHost(), base.getPort(), p + LoggedInFilter.LOGIN_URL, null, null);
            LOG.debug("Redirecting to " + redirect);
            return Response.temporaryRedirect(redirect).build();

        } catch (Exception e) {
            return logError(e);
        }
    }
}
