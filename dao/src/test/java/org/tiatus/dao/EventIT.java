package org.tiatus.dao;

import mockit.Mock;
import mockit.MockUp;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.entity.Event;
import org.tiatus.entity.Race;
import org.tiatus.entity.RaceEvent;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.transaction.*;
import java.util.List;

/**
 * Created by johnreynolds on 19/06/2016.
 */
public class EventIT {

    private static final Logger LOG = LoggerFactory.getLogger(EventIT.class);
    private EventDaoImpl dao;
    private EntityManager em;

    @Before
    public void setUp() throws Exception {
        dao = new EventDaoImpl();
        em = Persistence.createEntityManagerFactory("primary").createEntityManager();
        dao.em = em;
    }

    @After
    public void tearDown() throws Exception {
        em.close();
    }

    @Test
    public void getEvents() {
        List<Event> events = dao.getEvents();
        Assert.assertTrue(events.isEmpty());

        em.getTransaction().begin();
        Event event1 = new Event();
        event1.setId(1L);
        event1.setName("Event 1");
        em.merge(event1);
        Event event2 = new Event();
        event2.setId(2L);
        event2.setName("Event 2");
        em.merge(event2);
        em.getTransaction().commit();

        events = dao.getEvents();
        Assert.assertTrue(!events.isEmpty());
        Assert.assertTrue(events.size() == 2);
    }

    @Test
    public void getUnassignedEvents() {
        List<Event> events = dao.getEvents();
        Assert.assertTrue(events.isEmpty());
        List<Event> unassignedEvents = dao.getUnassignedEvents();
        Assert.assertTrue(unassignedEvents.isEmpty());

        em.getTransaction().begin();
        Event event1 = new Event();
        event1.setId(1L);
        event1.setName("Event 1");
        em.merge(event1);
        Event event2 = new Event();
        event2.setId(2L);
        event2.setName("Event 2");
        em.merge(event2);
        Race race1 = new Race();
        race1.setId(1L);
        race1.setName("Race 1");
        em.merge(race1);
        Event event3 = new Event();
        event3.setId(3L);
        event3.setName("Event 3");
        em.merge(event3);
        RaceEvent raceEvent1 = new RaceEvent();
        raceEvent1.setId(1L);
        raceEvent1.setRace(race1);
        raceEvent1.setEvent(event3);
        raceEvent1.setRaceEventOrder(1);
        em.merge(raceEvent1);
        em.getTransaction().commit();

        events = dao.getEvents();
        Assert.assertTrue(!events.isEmpty());
        Assert.assertTrue(events.size() == 3);

        unassignedEvents = dao.getUnassignedEvents();
        Assert.assertTrue(!unassignedEvents.isEmpty());
        Assert.assertTrue(unassignedEvents.size() == 2);
    }

    @Test
    public void addEvent() throws Exception {
        List<Event> events = dao.getEvents();
        Assert.assertTrue(events.isEmpty());
        Event event1 = new Event();
        event1.setId(1L);
        event1.setName("Event 1");
        dao.tx = new EntityUserTransaction(em);
        dao.addEvent(event1);

        events = dao.getEvents();
        Assert.assertTrue(!events.isEmpty());
        Assert.assertTrue(events.size() == 1);
    }


    @Test (expected = DaoException.class)
    public void addExistingEvent() throws Exception {
        List<Event> events = dao.getEvents();
        Assert.assertTrue(events.isEmpty());
        Event event1 = new Event();
        event1.setId(1L);
        event1.setName("Event 1");
        em.merge(event1);
        dao.tx = new EntityUserTransaction(em);
        dao.addEvent(event1);
        dao.addEvent(event1);
    }

    @Test (expected = DaoException.class)
    public void testAddEventWithException() throws Exception {
        List<Event> events = dao.getEvents();
        Assert.assertTrue(events.isEmpty());
        Event event1 = new Event();
        event1.setId(1L);
        event1.setName("Event 1");
        em.merge(event1);
        EntityManager em = new MockUp<EntityManager>(){
            @Mock
            public <T> T find(Class<T> entityClass, Object primaryKey) throws NotSupportedException {
                throw new NotSupportedException();
            }
        }.getMockInstance();
        dao.em = em;

        dao.addEvent(event1);
    }

    @Test
    public void removeEvent() throws Exception {
        List<Event> events = dao.getEvents();
        Assert.assertTrue(events.isEmpty());
        Event event1 = new Event();
        event1.setId(1L);
        event1.setName("Event 1");
        dao.tx = new EntityUserTransaction(em);
        dao.addEvent(event1);

        events = dao.getEvents();
        Assert.assertTrue(!events.isEmpty());
        Assert.assertTrue(events.size() == 1);

        dao.deleteEvent(event1);
        events = dao.getEvents();
        Assert.assertTrue(events.isEmpty());
    }

    @Test
    public void removeEventNotExisting() throws Exception {
        List<Event> events = dao.getEvents();
        Assert.assertTrue(events.isEmpty());
        Event event1 = new Event();
        event1.setId(1L);
        event1.setName("Event 1");
        dao.tx = new EntityUserTransaction(em);
        dao.addEvent(event1);

        events = dao.getEvents();
        Assert.assertTrue(!events.isEmpty());
        Assert.assertTrue(events.size() == 1);

        Event event2 = new Event();
        event2.setId(2L);
        event2.setName("Event 2");

        dao.deleteEvent(event2);
        events = dao.getEvents();
        Assert.assertTrue(!events.isEmpty());
        Assert.assertTrue(events.size() == 1);
        Assert.assertEquals(events.get(0).getName(), "Event 1");
    }

    @Test (expected = DaoException.class)
    public void removeRaceEventWithException() throws Exception {
        List<Event> events = dao.getEvents();
        Assert.assertTrue(events.isEmpty());
        Event event1 = new Event();
        event1.setId(1L);
        event1.setName("Event 1");
        EntityManager em = new MockUp<EntityManager>(){
            @Mock
            public <T> T find(Class<T> entityClass, Object primaryKey) throws NotSupportedException {
                throw new NotSupportedException();
            }
        }.getMockInstance();
        dao.em = em;
        dao.deleteEvent(event1);
    }

    @Test
    public void updateRaceEvent() throws Exception {
        List<Event> events = dao.getEvents();
        Assert.assertTrue(events.isEmpty());
        Event event1 = new Event();
        event1.setId(1L);
        event1.setName("Event 1");
        dao.tx = new EntityUserTransaction(em);
        dao.addEvent(event1);

        events = dao.getEvents();
        Assert.assertTrue(!events.isEmpty());
        Assert.assertTrue(events.size() == 1);
        Assert.assertEquals(events.get(0).getName(), "Event 1");

        event1.setName("new name");
        dao.updateEvent(event1);
        events = dao.getEvents();
        Assert.assertTrue(!events.isEmpty());
        Assert.assertTrue(events.size() == 1);
        Assert.assertEquals(events.get(0).getName(), "new name");
    }

    @Test (expected = DaoException.class)
    public void updateRaceEventWithException() throws Exception {
        List<Event> raceEvents = dao.getEvents();
        Assert.assertTrue(raceEvents.isEmpty());
        Event event1 = new Event();
        event1.setId(1L);
        event1.setName("Event 1");
        UserTransaction tx = new MockUp<UserTransaction>() {
            @Mock
            void commit() throws HeuristicMixedException, HeuristicRollbackException, IllegalStateException, RollbackException, SecurityException, SystemException {
                throw new HeuristicMixedException();
            }

        }.getMockInstance();
        dao.tx = tx;
        dao.updateEvent(event1);
    }
}
