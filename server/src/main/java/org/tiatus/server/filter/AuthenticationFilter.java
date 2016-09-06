package org.tiatus.server.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.auth.TiatusSecurityContext;
import org.tiatus.auth.UserPrincipal;
import org.tiatus.entity.Role;
import org.tiatus.entity.User;
import org.tiatus.entity.UserRole;
import org.tiatus.service.UserService;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.*;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.Providers;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import static javax.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED_TYPE;

/**
 * Created by johnreynolds on 26/08/2016.
 */
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {
    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationFilter.class);

    @Context
    HttpServletRequest servletRequest;

    @Context
    private Providers providers;

    @Inject
    private UserService userService;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        LOG.debug("in filter");

        if (!requestContext.hasEntity() || !requestContext.getMediaType().equals(APPLICATION_FORM_URLENCODED_TYPE)) {
            LOG.debug("not a form");
            return;
        }

        ByteArrayInputStream resettableIS = toResettableStream(requestContext.getEntityStream());

        Form form = providers.getMessageBodyReader(Form.class, Form.class, new Annotation[0], APPLICATION_FORM_URLENCODED_TYPE)
                .readFrom(Form.class, Form.class, new Annotation[0], APPLICATION_FORM_URLENCODED_TYPE, null, resettableIS);

        MultivaluedMap<String, String> parameters = form.asMap();

        resettableIS.reset();
        requestContext.setEntityStream(resettableIS);

        // are we already logged in the session
        HttpSession session = servletRequest.getSession();
        if (session != null){
            String scheme = requestContext.getUriInfo().getRequestUri().getScheme();
            UserPrincipal user;
            if (session.getAttribute("principal") == null) {
                LOG.debug("Creating new user principal");
                // do we have the auth details - in post
                user = getUser(parameters.get("user").get(0), parameters.get("pwd").get(0));
            } else {
                user = (UserPrincipal)session.getAttribute("principal");
            }
            if (user != null) {
                requestContext.setSecurityContext(new TiatusSecurityContext(user, scheme));
            }
        }
    }

    private UserPrincipal getUser(String userName, String password) {
        User user = userService.getUser(userName, password);
        if (user == null) {
            LOG.warn("Failed to login user");
            return null;
        }
        UserPrincipal principal = new UserPrincipal();
        principal.setUser(user);
        return principal;
    }

    private ByteArrayInputStream toResettableStream(InputStream entityStream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = entityStream.read(buffer)) > -1) {
            baos.write(buffer, 0, len);
        }
        baos.flush();
        return new ByteArrayInputStream(baos.toByteArray());
    }
}
