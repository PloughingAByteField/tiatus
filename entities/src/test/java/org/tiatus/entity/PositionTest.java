package org.tiatus.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Created by johnreynolds on 14/09/2016.
 */
public class PositionTest {
    @Test
    public void testPositionId() {
        Position position = new Position();
        position.setId(1L);
        Assertions.assertEquals(Long.valueOf(1L), position.getId());
    }

    @Test
    public void testPositionName() {
        Position position = new Position();
        position.setName("name");
        Assertions.assertEquals("name", position.getName());
    }

    @Test
    public void testPositionEqualsSameInstance() {
        Position position = new Position();
        Assertions.assertTrue(position.equals(position));
    }

    @Test
    public void testPositionEqualsDifferentType() {
        Position position = new Position();
        Assertions.assertFalse(position.equals(new User()));
    }

    @Test
    public void testPositionEqualsDifferentInstance() {
        Position position = new Position();
        position.setId(1L);
        position.setName("name");
        Position position2 = new Position();
        position2.setId(1L);
        position2.setName("name");
        Assertions.assertTrue(position.equals(position2));
    }

    @Test
    public void testPositionHashCode() {
        Position position = new Position();
        position.setId(1L);
        position.setName("name");
        Position position2 = new Position();
        position2.setId(1L);
        position2.setName("name");
        Assertions.assertEquals(position.hashCode(), position2.hashCode());
    }
}