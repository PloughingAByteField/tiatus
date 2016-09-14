package org.tiatus.entity;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by johnreynolds on 14/09/2016.
 */
public class UserTest {
    @Test
    public void testUserId() {
        User user = new User();
        user.setId(1L);
        Assert.assertEquals(user.getId(), Long.valueOf(1L));
    }

    @Test
    public void testUserName() {
        User user = new User();
        user.setUserName("username");
        Assert.assertEquals(user.getUserName(), "username");
    }

    @Test
    public void testPassword() {
        User user = new User();
        user.setPassword("password");
        Assert.assertEquals(user.getPassword(), "password");
    }

    @Test
    public void testUserFirstName() {
        User user = new User();
        user.setFirstName("name");
        Assert.assertEquals(user.getFirstName(), "name");
    }

    @Test
    public void testUserLastName() {
        User user = new User();
        user.setLastName("name");
        Assert.assertEquals(user.getLastName(), "name");
    }

    @Test
    public void testUserRoles() {
        User user = new User();
        UserRole userRole = new UserRole();
        Role role = new Role();
        role.setRoleName("test");
        userRole.setRole(role);
        userRole.setUser(user);
        Set<UserRole> userRoleList = new HashSet<>();
        userRoleList.add(userRole);
        user.setRoles(userRoleList);
        Assert.assertEquals(user.getRoles(), userRoleList);
    }
}
