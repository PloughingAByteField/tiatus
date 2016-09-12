package org.tiatus.auth;

import org.tiatus.entity.Role;
import org.tiatus.entity.UserRole;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

/**
 * SecurityContext to hold UserPrincipal used for checking a users roles
 */
public class TiatusSecurityContext implements SecurityContext {
    private UserPrincipal user;
    private boolean secure = false;

    /**
     * Construct TiatusSecurityContext for a user.
     * @param user UserPrincipal containing the users roles.
     * @param scheme The protocol used. HTTPS will mark connection as secure
     */
    public TiatusSecurityContext(UserPrincipal user, String scheme) {
        this.user = user;
        if ("https".equalsIgnoreCase(scheme)) {
            this.secure = true;
        }
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
        return secure;
    }

    @Override
    public String getAuthenticationScheme() {
        return null;
    }

    /**
     * Check if supplied UserPrincipal contains the supplied role
     * @param user The UserPrincipal to be checked.
     * @param roleString The role that is being checked for.
     * @return a <code>boolean</code> if user contains the role
     */
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
