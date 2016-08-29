package org.tiatus.auth;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

/**
 * Created by johnreynolds on 26/08/2016.
 */
public class TiatusSecurityContext implements SecurityContext {
    private UserPrincipal user;
    private String scheme;

    public TiatusSecurityContext(UserPrincipal user, String scheme) {
        this.user = user;
        this.scheme = scheme;
    }

    @Override
    public Principal getUserPrincipal() {
        return user;
    }

    @Override
    public boolean isUserInRole(String role) {
        if (user != null && user.getUser() != null && user.getUser().getRoles() != null) {
            return user.getUser().getRoles().contains(role);
        }
        return false;
    }

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public String getAuthenticationScheme() {
        return null;
    }
}
