package org.tiatus.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Created by johnreynolds on 14/09/2016.
 */
public class RoleTest {
    @Test
    public void testRoleId() {
        Role role = new Role();
        role.setId(1L);
        Assertions.assertEquals(Long.valueOf(1L), role.getId());
    }

    @Test
    public void testRoleName() {
        Role role = new Role();
        role.setRoleName("name");
        Assertions.assertEquals("name", role.getRoleName());
    }

    @Test
    public void testRoleEqualsSameInstance() {
        Role role = new Role();
        role.setId(1L);
        role.setRoleName("name");
        Assertions.assertTrue(role.equals(role));
    }

    @Test
    public void testRoleEqualsDifferentType() {
        Role role = new Role();
        role.setId(1L);
        role.setRoleName("name");
        Assertions.assertFalse(role.equals(new User()));
    }

    @Test
    public void testRoleEqualsNewInstance() {
        Role role = new Role();
        role.setId(1L);
        role.setRoleName("name");
        Role role2 = new Role();
        role2.setId(1L);
        role2.setRoleName("name");
        Assertions.assertTrue(role.equals(role2));
    }
}
