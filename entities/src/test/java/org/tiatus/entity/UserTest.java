package org.tiatus.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
        Assertions.assertEquals(Long.valueOf(1L), user.getId());
    }

    @Test
    public void testUserName() {
        User user = new User();
        user.setUserName("username");
        Assertions.assertEquals("username", user.getUserName());
    }

    @Test
    public void testPassword() {
        User user = new User();
        user.setPassword("password");
        Assertions.assertEquals("password", user.getPassword());
    }

    @Test
    public void testUserFirstName() {
        User user = new User();
        user.setFirstName("name");
        Assertions.assertEquals("name", user.getFirstName());
    }

    @Test
    public void testUserLastName() {
        User user = new User();
        user.setLastName("name");
        Assertions.assertEquals("name", user.getLastName());
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
        Assertions.assertEquals(user.getRoles(), userRoleList);
    }
}
