package org.tiatus.entity;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by johnreynolds on 14/09/2016.
 */
public class RoleTest {
    @Test
    public void testRoleId() {
        Role role = new Role();
        role.setId(1L);
        Assert.assertEquals(role.getId(), Long.valueOf(1L));
    }

    @Test
    public void testRoleName() {
        Role role = new Role();
        role.setRoleName("name");
        Assert.assertEquals(role.getRoleName(), "name");
    }

    @Test
    public void testRoleEqualsSameInstance() {
        Role role = new Role();
        role.setId(1L);
        role.setRoleName("name");
        Assert.assertTrue(role.equals(role));
    }

    @Test
    public void testRoleEqualsDifferentType() {
        Role role = new Role();
        role.setId(1L);
        role.setRoleName("name");
        Assert.assertFalse(role.equals(new User()));
    }

    @Test
    public void testRoleEqualsNewInstance() {
        Role role = new Role();
        role.setId(1L);
        role.setRoleName("name");
        Role role2 = new Role();
        role2.setId(1L);
        role2.setRoleName("name");
        Assert.assertTrue(role.equals(role2));
    }
}
