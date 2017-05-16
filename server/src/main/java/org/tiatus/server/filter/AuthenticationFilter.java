package org.tiatus.server.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.auth.TiatusSecurityContext;
import org.tiatus.auth.UserPrincipal;
import org.tiatus.entity.User;
import org.tiatus.service.UserService;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Priorities;
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

    private UserService userService;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        UserPrincipal user;
        String scheme = requestContext.getUriInfo().getRequestUri().getScheme();
        LOG.debug("In AuthenticationFilter");
        // are we already logged in the session
        HttpSession session = servletRequest.getSession(false);
        if (session == null) {
            LOG.debug("No session");
            user = getUserFromParameters(requestContext);

        } else {
            LOG.debug("Have session");
            user = (UserPrincipal)session.getAttribute("principal");
            if (user == null) {
                user = getUserFromParameters(requestContext);
            }
        }

        if (user != null) {
            requestContext.setSecurityContext(new TiatusSecurityContext(user, scheme));
        }
    }

    private UserPrincipal getUserFromParameters(ContainerRequestContext requestContext) throws IOException {
        if (!requestContext.hasEntity() || !requestContext.getMediaType().equals(APPLICATION_FORM_URLENCODED_TYPE)) {
            LOG.debug("not a form");
            return null;
        }

        LOG.debug("Get user from form");
        Form form = getForm(requestContext);
        MultivaluedMap<String, String> parameters = form.asMap();

        // do we have the auth details - in post
        if (parameters.get("user") == null || parameters.get("pwd") == null) {
            LOG.warn("Do not have user or pwd set in the form");
            return null;
        }

        return getUser(parameters.get("user").get(0), parameters.get("pwd").get(0));
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

    private Form getForm(ContainerRequestContext requestContext) throws IOException {
        ByteArrayInputStream resettableIS = toResettableStream(requestContext.getEntityStream());

        Form form = providers.getMessageBodyReader(Form.class, Form.class, new Annotation[0], APPLICATION_FORM_URLENCODED_TYPE)
                .readFrom(Form.class, Form.class, new Annotation[0], APPLICATION_FORM_URLENCODED_TYPE, null, resettableIS);

        resettableIS.reset();
        requestContext.setEntityStream(resettableIS);
        return form;
    }

    @Inject
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
