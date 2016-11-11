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
    public void testPositionActive() {
        Position position = new Position();
        position.setActive(true);
        Assert.assertTrue(position.isActive());
    }

    @Test
    public void testPositionShowAllEntries() {
        Position position = new Position();
        position.setShowAllEntries(true);
        Assert.assertTrue(position.isShowAllEntries());
    }

    @Test
    public void testPositionTiming() {
        Position position = new Position();
        position.setTiming(true);
        Assert.assertTrue(position.isTiming());
    }

    @Test
    public void testPositionCansStart() {
        Position position = new Position();
        position.setCanStart(true);
        Assert.assertTrue(position.isCanStart());
    }

    @Test
    public void testPositionOrder() {
        Position position = new Position();
        position.setOrder(1);
        Assert.assertEquals(position.getOrder(), Integer.valueOf(1));
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
        position.setOrder(1);
        position.setShowAllEntries(true);
        position.setTiming(true);
        position.setActive(true);
        position.setCanStart(true);
        position.setName("name");
        Position position2 = new Position();
        position2.setId(1L);
        position2.setOrder(1);
        position2.setShowAllEntries(true);
        position2.setTiming(true);
        position2.setActive(true);
        position2.setCanStart(true);
        position2.setName("name");
        Assert.assertTrue(position.equals(position2));
    }

    @Test
    public void testPositionHashCode() {
        Position position = new Position();
        position.setId(1L);
        position.setOrder(1);
        position.setShowAllEntries(true);
        position.setTiming(true);
        position.setActive(true);
        position.setCanStart(true);
        position.setName("name");
        Position position2 = new Position();
        position2.setId(1L);
        position2.setOrder(1);
        position2.setShowAllEntries(true);
        position2.setTiming(true);
        position2.setActive(true);
        position2.setCanStart(true);
        position2.setName("name");
        Assert.assertEquals(position.hashCode(), position2.hashCode());
    }
}