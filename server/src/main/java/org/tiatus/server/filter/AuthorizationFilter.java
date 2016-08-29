package org.tiatus.server.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.security.Principal;

/**
 * Created by johnreynolds on 26/08/2016.
 */
@Provider
@Priority(Priorities.AUTHORIZATION)
public class AuthorizationFilter implements ContainerRequestFilter {
    private static final Logger LOG = LoggerFactory.getLogger(AuthorizationFilter.class);

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        LOG.debug("In AuthorizationFilter");
        SecurityContext sc = requestContext.getSecurityContext();
        Principal p = sc.getUserPrincipal();
        sc.isUserInRole("ADMIN");
    }
}
