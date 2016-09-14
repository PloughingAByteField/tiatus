package org.tiatus.entity;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by johnreynolds on 14/09/2016.
 */
public class RaceTest {
    @Test
    public void testRaceId() {
        Race race = new Race();
        race.setId(1L);
        Assert.assertEquals(race.getId(), Long.valueOf(1L));
    }

    @Test
    public void testRaceName() {
        Race race = new Race();
        race.setName("name");
        Assert.assertEquals(race.getName(), "name");
    }

    @Test
    public void testRaceStartTime() {
        Race race = new Race();
        race.setStartTime("time");
        Assert.assertEquals(race.getStartTime(), "time");
    }

    @Test
    public void testRaceActive() {
        Race race = new Race();
        race.setActive(true);
        Assert.assertTrue(race.isActive());
    }

    @Test
    public void testRaceClosed() {
        Race race = new Race();
        race.setClosed(true);
        Assert.assertTrue(race.isClosed());
    }

    @Test
    public void testRaceDrawLocked() {
        Race race = new Race();
        race.setDrawLocked(true);
        Assert.assertTrue(race.isDrawLocked());
    }

    @Test
    public void testRaceOrder() {
        Race race = new Race();
        race.setRaceOrder(1);
        Assert.assertEquals(race.getRaceOrder(), 1);
    }

    @Test
    public void testRaceEqualsSameInstance() {
        Race race = new Race();
        Assert.assertTrue(race.equals(race));
    }

    @Test
    public void testRaceEqualsDifferentType() {
        Race race = new Race();
        Assert.assertFalse(race.equals(new User()));
    }

    @Test
    public void testRaceEqualsDifferentInstance() {
        Race race = new Race();
        race.setId(1L);
        race.setRaceOrder(1);
        race.setClosed(true);
        race.setActive(true);
        race.setDrawLocked(true);
        race.setStartTime("time");
        race.setName("name");
        Race race2 = new Race();
        race2.setId(1L);
        race2.setRaceOrder(1);
        race2.setClosed(true);
        race2.setActive(true);
        race2.setDrawLocked(true);
        race2.setStartTime("time");
        race2.setName("name");
        Assert.assertTrue(race.equals(race2));
    }

    @Test
    public void testRaceHashCode() {
        Race race = new Race();
        race.setId(1L);
        race.setRaceOrder(1);
        race.setClosed(true);
        race.setActive(true);
        race.setDrawLocked(true);
        race.setStartTime("time");
        race.setName("name");
        Race race2 = new Race();
        race2.setId(1L);
        race2.setRaceOrder(1);
        race2.setClosed(true);
        race2.setActive(true);
        race2.setDrawLocked(true);
        race2.setStartTime("time");
        race2.setName("name");
        Assert.assertEquals(race.hashCode(), race2.hashCode());
    }
}