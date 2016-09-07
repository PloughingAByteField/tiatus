package org.tiatus.auth;

import org.tiatus.entity.Role;
import org.tiatus.entity.UserRole;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

/**
 * Created by johnreynolds on 26/08/2016.
 */
public class TiatusSecurityContext implements SecurityContext {
    private UserPrincipal user;

    public TiatusSecurityContext(UserPrincipal user) {
        this.user = user;
    }

    @Override
    public Principal getUserPrincipal() {
        return user;
    }

    @Override
    public boolean isUserInRole(String roleString) {
        return isUserInRole(user, roleString);
    }

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public String getAuthenticationScheme() {
        return null;
    }

    public static boolean isUserInRole(UserPrincipal user, String roleString) {
        if (user != null && user.getUser() != null && user.getUser().getRoles() != null) {
            Role role = new Role();
            role.setRoleName(roleString);
            return isUserInRole(user, role);
        }
        return false;
    }

    private static boolean isUserInRole(UserPrincipal user, Role role) {
        if (user != null && user.getUser() != null && user.getUser().getRoles() != null) {
            UserRole userRole = new UserRole();
            userRole.setRole(role);
            return user.getUser().getRoles().contains(userRole);
        }
        return false;
    }
}
