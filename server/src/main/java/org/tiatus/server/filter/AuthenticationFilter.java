package org.tiatus.server.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.auth.TiatusSecurityContext;
import org.tiatus.auth.UserPrincipal;
import org.tiatus.entity.Role;
import org.tiatus.entity.User;
import org.tiatus.entity.UserRole;

import javax.annotation.Priority;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by johnreynolds on 26/08/2016.
 */
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {
    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationFilter.class);

    @Context
    HttpServletRequest servletRequest;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        LOG.debug("in filter");

        // are we already logged in the session
        HttpSession session = servletRequest.getSession();
        if (session != null){
            String scheme = requestContext.getUriInfo().getRequestUri().getScheme();
            UserPrincipal user;
            if (session.getAttribute("principal") == null) {
                LOG.debug("Creating new user principal");
                // do we have the auth details - in post
                user = getUser(null, null);
            } else {
                user = (UserPrincipal)session.getAttribute("principal");
            }
            requestContext.setSecurityContext(new TiatusSecurityContext(user, scheme));
        }
    }

    private UserPrincipal getUser(String userName, String password) {
        UserPrincipal principal = new UserPrincipal();
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Reynolds");
        user.setUserName("admin");
        Role role = new Role();
        role.setRole("ADMIN");
        UserRole userRole = new UserRole();
        userRole.setRole(role);
        Set<UserRole> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);
        principal.setUser(user);
        return principal;
    }
}
