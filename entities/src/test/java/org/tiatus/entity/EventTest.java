package org.tiatus.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Created by johnreynolds on 14/09/2016.
 */
public class EventTest {
    @Test
    public void testId() {
        Event event = new Event();
        event.setId(1L);
        Assertions.assertEquals(Long.valueOf(1L), event.getId());
    }

    @Test
    public void testName() {
        Event event = new Event();
        event.setName("name");
        Assertions.assertEquals("name", event.getName());
    }

    @Test
    public void testWeighted() {
        Event event = new Event();
        event.setWeighted(true);
        Assertions.assertTrue(event.isWeighted());
        event.setWeighted(false);
        Assertions.assertFalse(event.isWeighted());
    }

    @Test
    public void testEventEqualsSameInstance() {
        Event event = new Event();
        Assertions.assertTrue(event.equals(event));
    }

    @Test
    public void testEventEqualsDifferentType() {
        Event event = new Event();
        Assertions.assertFalse(event.equals(new User()));
    }

    @Test
    public void testEqualsDifferentInstance() {
        Event event = new Event();
        event.setId(1L);
        event.setName("name");
        event.setWeighted(true);
        Event event2 = new Event();
        event2.setId(1L);
        event2.setName("name");
        event2.setWeighted(true);
        Assertions.assertTrue(event.equals(event2));
    }

    @Test
    public void testRaceEventHashCode() {
        Event event = new Event();
        event.setId(1L);
        event.setName("name");
        event.setWeighted(true);
        Event event2 = new Event();
        event2.setId(1L);
        event2.setName("name");
        event2.setWeighted(true);
        Assertions.assertEquals(event.hashCode(), event2.hashCode());
    }
}