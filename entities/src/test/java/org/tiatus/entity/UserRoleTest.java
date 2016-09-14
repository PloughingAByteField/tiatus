package org.tiatus.entity;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by johnreynolds on 14/09/2016.
 */
public class UserRoleTest {
    @Test
    public void testUserRoleId() {
        UserRole userRole = new UserRole();
        userRole.setId(1L);
        Assert.assertEquals(userRole.getId(), Long.valueOf(1L));
    }

    @Test
    public void testUserRoleRole() {
        UserRole userRole = new UserRole();
        Role role = new Role();
        role.setRoleName("test");
        userRole.setRole(role);
        Assert.assertEquals(userRole.getRole(), role);
    }

    @Test
    public void testUserRoleUser() {
        UserRole userRole = new UserRole();
        User user = new User();
        userRole.setUser(user);
        Assert.assertEquals(userRole.getUser(), user);
    }

    @Test
    public void testUserRoleEqualsSameInstance() {
        UserRole userRole = new UserRole();
        Assert.assertTrue(userRole.equals(userRole));
    }

    @Test
    public void testUserRoleEqualsDifferentType() {
        UserRole userRole = new UserRole();
        Assert.assertFalse(userRole.equals(new User()));
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
        Assert.assertTrue(userRole.equals(userRole2));
    }
}
