package org.tiatus.entity;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by johnreynolds on 14/09/2016.
 */
public class EventTest {
    @Test
    public void testId() {
        Event event = new Event();
        event.setId(1L);
        Assert.assertEquals(event.getId(), Long.valueOf(1L));
    }

    @Test
    public void testName() {
        Event event = new Event();
        event.setName("name");
        Assert.assertEquals(event.getName(), "name");
    }

    @Test
    public void testWeighted() {
        Event event = new Event();
        event.setWeighted(true);
        Assert.assertTrue(event.isWeighted());
        event.setWeighted(false);
        Assert.assertFalse(event.isWeighted());
    }

    @Test
    public void testEventEqualsSameInstance() {
        Event event = new Event();
        Assert.assertTrue(event.equals(event));
    }

    @Test
    public void testEventEqualsDifferentType() {
        Event event = new Event();
        Assert.assertFalse(event.equals(new User()));
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
        Assert.assertTrue(event.equals(event2));
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
        Assert.assertEquals(event.hashCode(), event2.hashCode());
    }
}