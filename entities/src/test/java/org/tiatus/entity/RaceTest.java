package org.tiatus.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


/**
 * Created by johnreynolds on 14/09/2016.
 */
public class RaceTest {
    @Test
    public void testRaceId() {
        Race race = new Race();
        race.setId(1L);
        Assertions.assertEquals(Long.valueOf(1L), race.getId());
    }

    @Test
    public void testRaceName() {
        Race race = new Race();
        race.setName("name");
        Assertions.assertEquals("name", race.getName());
    }

    @Test
    public void testRaceStartTime() {
        Race race = new Race();
        race.setStartTime("time");
        Assertions.assertEquals("time", race.getStartTime());
    }

    @Test
    public void testRaceActive() {
        Race race = new Race();
        race.setActive(true);
        Assertions.assertTrue(race.isActive());
    }

    @Test
    public void testRaceClosed() {
        Race race = new Race();
        race.setClosed(true);
        Assertions.assertTrue(race.isClosed());
    }

    @Test
    public void testRaceDrawLocked() {
        Race race = new Race();
        race.setDrawLocked(true);
        Assertions.assertTrue(race.isDrawLocked());
    }

    @Test
    public void testRaceOrder() {
        Race race = new Race();
        race.setRaceOrder(1);
        Assertions.assertEquals(1, race.getRaceOrder());
    }

    @Test
    public void testRaceEqualsSameInstance() {
        Race race = new Race();
        Assertions.assertTrue(race.equals(race));
    }

    @Test
    public void testRaceEqualsDifferentType() {
        Race race = new Race();
        Assertions.assertFalse(race.equals(new User()));
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
        Assertions.assertTrue(race.equals(race2));
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
        Assertions.assertEquals(race.hashCode(), race2.hashCode());
    }
}