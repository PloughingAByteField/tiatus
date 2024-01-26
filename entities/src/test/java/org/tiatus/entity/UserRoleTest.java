package org.tiatus.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Created by johnreynolds on 14/09/2016.
 */
public class UserRoleTest {
    @Test
    public void testUserRoleId() {
        UserRole userRole = new UserRole();
        userRole.setId(1L);
        Assertions.assertEquals(Long.valueOf(1L), userRole.getId());
    }

    @Test
    public void testUserRoleRole() {
        UserRole userRole = new UserRole();
        Role role = new Role();
        role.setRoleName("test");
        userRole.setRole(role);
        Assertions.assertEquals(role, userRole.getRole());
    }

    @Test
    public void testUserRoleUser() {
        UserRole userRole = new UserRole();
        User user = new User();
        userRole.setUser(user);
        Assertions.assertEquals(user, userRole.getUser());
    }

    @Test
    public void testUserRoleEqualsSameInstance() {
        UserRole userRole = new UserRole();
        Assertions.assertTrue(userRole.equals(userRole));
    }

    @Test
    public void testUserRoleEqualsDifferentType() {
        UserRole userRole = new UserRole();
        Assertions.assertFalse(userRole.equals(new User()));
    }

    @Test
    public void testUserRoleEqualsDifferentInstance() {
        UserRole userRole = new UserRole();
        Role role = new Role();
        role.setRoleName("test");
        userRole.setRole(role);
        User user = new User();
        userRole.setUser(user);
        UserRole userRole2 = new UserRole();
        userRole2.setUser(user);
        userRole2.setRole(role);
        Assertions.assertTrue(userRole.equals(userRole2));
    }
}
