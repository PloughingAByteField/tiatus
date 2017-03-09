package org.tiatus.entity;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by johnreynolds on 14/09/2016.
 */
public class PositionTest {
    @Test
    public void testPositionId() {
        Position position = new Position();
        position.setId(1L);
        Assert.assertEquals(position.getId(), Long.valueOf(1L));
    }

    @Test
    public void testPositionName() {
        Position position = new Position();
        position.setName("name");
        Assert.assertEquals(position.getName(), "name");
    }

    @Test
    public void testPositionEqualsSameInstance() {
        Position position = new Position();
        Assert.assertTrue(position.equals(position));
    }

    @Test
    public void testPositionEqualsDifferentType() {
        Position position = new Position();
        Assert.assertFalse(position.equals(new User()));
    }

    @Test
    public void testPositionEqualsDifferentInstance() {
        Position position = new Position();
        position.setId(1L);
        position.setName("name");
        Position position2 = new Position();
        position2.setId(1L);
        position2.setName("name");
        Assert.assertTrue(position.equals(position2));
    }

    @Test
    public void testPositionHashCode() {
        Position position = new Position();
        position.setId(1L);
        position.setName("name");
        Position position2 = new Position();
        position2.setId(1L);
        position2.setName("name");
        Assert.assertEquals(position.hashCode(), position2.hashCode());
    }
}