package org.tiatus.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Created by johnreynolds on 14/09/2016.
 */
public class RaceEventTest {
    @Test
    public void testRaceEventId() {
        RaceEvent raceEvent = new RaceEvent();
        raceEvent.setId(1L);
        Assertions.assertEquals(Long.valueOf(1L), raceEvent.getId());
    }

    @Test
    public void testRaceId() {
        RaceEvent raceEvent = new RaceEvent();
        Race race = new Race();
        race.setId(1L);
        raceEvent.setRace(race);
        Assertions.assertEquals(Long.valueOf(1L), raceEvent.getRace().getId());
    }

    @Test
    public void testRaceName() {
        RaceEvent raceEvent = new RaceEvent();
        Race race = new Race();
        race.setName("name");
        raceEvent.setRace(race);
        Assertions.assertEquals("name", raceEvent.getRace().getName());
    }

    @Test
    public void testEventId() {
        RaceEvent raceEvent = new RaceEvent();
        Event event = new Event();
        event.setId(1L);
        raceEvent.setEvent(event);
        Assertions.assertEquals(Long.valueOf(1L), raceEvent.getEvent().getId());
    }

    @Test
    public void testEventName() {
        RaceEvent raceEvent = new RaceEvent();
        Event event = new Event();
        event.setName("name");
        raceEvent.setEvent(event);
        Assertions.assertEquals("name", raceEvent.getEvent().getName());
    }


    @Test
    public void testRaceOrder() {
        RaceEvent raceEvent = new RaceEvent();
        raceEvent.setRaceEventOrder(1);
        Assertions.assertEquals(1, raceEvent.getRaceEventOrder());
    }

    @Test
    public void testRaceEventEqualsSameInstance() {
        RaceEvent raceEvent = new RaceEvent();
        Assertions.assertTrue(raceEvent.equals(raceEvent));
    }

    @Test
    public void testRaceEventEqualsDifferentType() {
        RaceEvent raceEvent = new RaceEvent();
        Assertions.assertFalse(raceEvent.equals(new User()));
    }

    @Test
    public void testRaceEqualsDifferentInstance() {
        Race race = new Race();
        race.setId(1L);
        race.setName("name");
        Race race2 = new Race();
        race2.setId(1L);
        race2.setName("name");
        Assertions.assertTrue(race.equals(race2));

        Event event = new Event();
        event.setId(1L);
        event.setName("name");
        Event event2 = new Event();
        event2.setId(1L);
        event2.setName("name");
        Assertions.assertTrue(event.equals(event2));

        RaceEvent raceEvent = new RaceEvent();
        raceEvent.setId(1L);
        raceEvent.setRaceEventOrder(1);
        raceEvent.setEvent(event);
        raceEvent.setRace(race);

        RaceEvent raceEvent2 = new RaceEvent();
        raceEvent2.setId(1L);
        raceEvent2.setRaceEventOrder(1);
        raceEvent2.setEvent(event2);
        raceEvent2.setRace(race2);

        Assertions.assertTrue(raceEvent.equals(raceEvent2));
    }

    @Test
    public void testRaceEventHashCode() {
        Race race = new Race();
        race.setId(1L);
        race.setName("name");
        Race race2 = new Race();
        race2.setId(1L);
        race2.setName("name");
        Assertions.assertTrue(race.equals(race2));

        Event event = new Event();
        event.setId(1L);
        event.setName("name");
        Event event2 = new Event();
        event2.setId(1L);
        event2.setName("name");
        Assertions.assertTrue(event.equals(event2));

        RaceEvent raceEvent = new RaceEvent();
        raceEvent.setId(1L);
        raceEvent.setRaceEventOrder(1);
        raceEvent.setEvent(event);
        raceEvent.setRace(race);

        RaceEvent raceEvent2 = new RaceEvent();
        raceEvent2.setId(1L);
        raceEvent2.setRaceEventOrder(1);
        raceEvent2.setEvent(event2);
        raceEvent2.setRace(race2);
        Assertions.assertEquals(raceEvent.hashCode(), raceEvent2.hashCode());
    }
}