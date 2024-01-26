package org.tiatus.auth;

import org.tiatus.entity.Role;
import org.tiatus.entity.User;
import org.tiatus.entity.UserRole;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by johnreynolds on 14/09/2016.
 */
public class TiatusSecurityContextTest {

    // @Test
    // public void testGetUserPrincipal() {
    //     UserPrincipal userPrincipal = new UserPrincipal();
    //     TiatusSecurityContext sc = new TiatusSecurityContext(userPrincipal, "https");
    //     Assert.assertEquals(sc.getUserPrincipal(), userPrincipal);
    // }

    // @Test
    // public void testIsSecureForHttps() {
    //     UserPrincipal userPrincipal = new UserPrincipal();
    //     TiatusSecurityContext sc = new TiatusSecurityContext(userPrincipal, "https");
    //     Assert.assertTrue(sc.isSecure());
    // }

    // @Test
    // public void testIsNotSecureForHttp() {
    //     UserPrincipal userPrincipal = new UserPrincipal();
    //     TiatusSecurityContext sc = new TiatusSecurityContext(userPrincipal, "http");
    //     Assert.assertFalse(sc.isSecure());
    // }

    // @Test
    // public void testGetAuthenticationScheme() {
    //     UserPrincipal userPrincipal = new UserPrincipal();
    //     TiatusSecurityContext sc = new TiatusSecurityContext(userPrincipal, "https");
    //     Assert.assertNull(sc.getAuthenticationScheme());
    // }

    // @Test
    // public void testIsUserInRole() {
    //     UserPrincipal userPrincipal = createUserPrincipalWithRole("ADMIN");
    //     TiatusSecurityContext sc = new TiatusSecurityContext(userPrincipal, "https");
    //     Assert.assertTrue(sc.isUserInRole("ADMIN"));
    // }

    // @Test
    // public void testIsUserNotInRole() {
    //     UserPrincipal userPrincipal = createUserPrincipalWithRole("ADMIN");
    //     TiatusSecurityContext sc = new TiatusSecurityContext(userPrincipal, "https");
    //     Assert.assertFalse(sc.isUserInRole("BAD"));
    // }

    // @Test
    // public void testIsUserInRoleForNullUserPrincipal() {
    //     TiatusSecurityContext sc = new TiatusSecurityContext(null, "http");
    //     Assert.assertFalse(sc.isUserInRole("BAD"));
    // }

    // @Test
    // public void testIsUserInRoleForUserPrincipalNoRoles() {
    //     TiatusSecurityContext sc = new TiatusSecurityContext(null, "http");
    //     Assert.assertFalse(sc.isUserInRole("BAD"));
    // }

    // @Test
    // public void testIsUserInRoleForUserPrincipalAndRole() {
    //     UserPrincipal userPrincipal = createUserPrincipalWithRole(null);
    //     TiatusSecurityContext sc = new TiatusSecurityContext(userPrincipal, "http");
    //     Assert.assertFalse(sc.isUserInRole("BAD"));
    // }

    // @Test
    // public void testIsUserInRoleForNullUserPrincipalAndRole() {
    //     Assert.assertFalse(TiatusSecurityContext.isUserInRole(null, "ADMIN"));
    // }

    // private UserPrincipal createUserPrincipalWithRole(String roleString) {
    //     UserPrincipal userPrincipal = new UserPrincipal();
    //     User user = new User();
    //     user.setUserName("user");
    //     if (null != roleString) {
    //         UserRole userRole = new UserRole();
    //         Role role = new Role();
    //         role.setRoleName(roleString);
    //         userRole.setRole(role);
    //         userRole.setUser(user);
    //         Set<UserRole> userRoleList = new HashSet<>();
    //         userRoleList.add(userRole);
    //         user.setRoles(userRoleList);
    //     }
    //     userPrincipal.setUser(user);
    //     return userPrincipal;
    // }
}
