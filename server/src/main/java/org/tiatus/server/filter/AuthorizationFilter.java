package org.tiatus.server.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import javax.annotation.security.DenyAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by johnreynolds on 26/08/2016.
 */
@Provider
@Priority(Priorities.AUTHORIZATION)
public class AuthorizationFilter implements ContainerRequestFilter {
    private static final Logger LOG = LoggerFactory.getLogger(AuthorizationFilter.class);

    @Context
    ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        LOG.debug("In AuthorizationFilter");
        SecurityContext sc = requestContext.getSecurityContext();

        Method method = resourceInfo.getResourceMethod();

        if (method.isAnnotationPresent(DenyAll.class)) {
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        }

        if (method.isAnnotationPresent(RolesAllowed.class)) {
            if (sc.getUserPrincipal() == null) {
                LOG.debug("not logged in when accessing restricted role");
                throw new WebApplicationException(Response.Status.FORBIDDEN);
            }

            RolesAllowed rolesAnnotation = method.getAnnotation(RolesAllowed.class);
            Set<String> rolesAllowed = new HashSet<>(Arrays.asList(rolesAnnotation.value()));
            for (String role : rolesAllowed) {
                LOG.debug("Have allowed role " + role);
                if (sc.isUserInRole(role)) {
                    return;
                }
            }

            LOG.warn("Failed to find a role for user " + sc.getUserPrincipal().getName() + " for url: " + requestContext.getUriInfo().getRequestUri());
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        }
    }
}
