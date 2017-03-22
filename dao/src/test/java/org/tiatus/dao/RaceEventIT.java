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
public class RaceEventIT {

    private static final Logger LOG = LoggerFactory.getLogger(RaceEventIT.class);
    private RaceEventDaoImpl dao;
    private EntityManager em;

    @Before
    public void setUp() throws Exception {
        dao = new RaceEventDaoImpl();
        em = Persistence.createEntityManagerFactory("primary").createEntityManager();
        dao.em = em;
    }

    @After
    public void tearDown() throws Exception {
        em.close();
    }

    @Test
    public void getRaceEvents() {
        List<RaceEvent> raceEvents = dao.getRaceEvents();
        Assert.assertTrue(raceEvents.isEmpty());

        // add race
        em.getTransaction().begin();
        Race race1 = new Race();
        race1.setId(1L);
        race1.setName("Race 1");
        Race race2 = new Race();
        race2.setId(2L);
        race2.setName("Race 2");
        em.merge(race1);
        em.merge(race2);
        Event event1 = new Event();
        event1.setId(1L);
        event1.setName("Event 1");
        em.merge(event1);
        Event event2 = new Event();
        event2.setId(2L);
        event2.setName("Event 2");
        em.merge(event2);

        RaceEvent raceEvent1 = new RaceEvent();
        raceEvent1.setId(1L);
        raceEvent1.setRace(race1);
        raceEvent1.setEvent(event1);
        em.merge(raceEvent1);
        RaceEvent raceEvent2 = new RaceEvent();
        raceEvent2.setId(2L);
        raceEvent2.setRace(race2);
        raceEvent2.setEvent(event2);
        em.merge(raceEvent2);

        em.getTransaction().commit();

        raceEvents = dao.getRaceEvents();
        Assert.assertTrue(!raceEvents.isEmpty());
        Assert.assertTrue(raceEvents.size() == 2);
    }

    @Test
    public void addRaceEvent() throws Exception {
        List<RaceEvent> raceEvents = dao.getRaceEvents();
        Assert.assertTrue(raceEvents.isEmpty());
        Race race1 = new Race();
        race1.setId(1L);
        race1.setName("Race 1");
        em.merge(race1);
        Event event1 = new Event();
        event1.setName("Event 1");
        RaceEvent raceEvent1 = new RaceEvent();
        raceEvent1.setId(1L);
        raceEvent1.setRace(race1);
        raceEvent1.setEvent(event1);
        dao.tx = new EntityUserTransaction(em);
        dao.addRaceEvent(raceEvent1);

        raceEvents = dao.getRaceEvents();
        Assert.assertTrue(!raceEvents.isEmpty());
        Assert.assertTrue(raceEvents.size() == 1);
    }


    @Test (expected = DaoException.class)
    public void addExistingRaceEvent() throws Exception {
        List<RaceEvent> raceEvents = dao.getRaceEvents();
        Assert.assertTrue(raceEvents.isEmpty());
        Race race1 = new Race();
        race1.setId(1L);
        race1.setName("Race 1");
        em.merge(race1);
        Event event1 = new Event();
        event1.setId(1L);
        event1.setName("Event 1");
        em.merge(event1);
        RaceEvent raceEvent1 = new RaceEvent();
        raceEvent1.setId(1L);
        raceEvent1.setRace(race1);
        raceEvent1.setEvent(event1);
        dao.tx = new EntityUserTransaction(em);
        dao.addRaceEvent(raceEvent1);
        dao.addRaceEvent(raceEvent1);
    }

    @Test (expected = DaoException.class)
    public void testAddRaceEventWithException() throws Exception {
        List<RaceEvent> raceEvents = dao.getRaceEvents();
        Assert.assertTrue(raceEvents.isEmpty());
        Race race1 = new Race();
        race1.setId(1L);
        race1.setName("Race 1");
        em.merge(race1);
        Event event1 = new Event();
        event1.setName("Event 1");
        RaceEvent raceEvent1 = new RaceEvent();
        raceEvent1.setId(1L);
        raceEvent1.setRace(race1);
        raceEvent1.setEvent(event1);
        EntityManager em = new MockUp<EntityManager>(){
            @Mock
            public <T> T find(Class<T> entityClass, Object primaryKey) throws NotSupportedException {
                throw new NotSupportedException();
            }
        }.getMockInstance();
        dao.tx = new EntityUserTransaction(em);
        dao.em = em;
        dao.addRaceEvent(raceEvent1);
    }

    @Test
    public void removeRaceEvent() throws Exception {
        List<RaceEvent> raceEvents = dao.getRaceEvents();
        Assert.assertTrue(raceEvents.isEmpty());
        Race race1 = new Race();
        race1.setId(1L);
        race1.setName("Race 1");
        em.merge(race1);
        Event event1 = new Event();
        event1.setName("Event 1");
        RaceEvent raceEvent1 = new RaceEvent();
        raceEvent1.setId(1L);
        raceEvent1.setRace(race1);
        raceEvent1.setEvent(event1);
        dao.tx = new EntityUserTransaction(em);
        dao.addRaceEvent(raceEvent1);

        raceEvents = dao.getRaceEvents();
        Assert.assertTrue(!raceEvents.isEmpty());
        Assert.assertTrue(raceEvents.size() == 1);

        dao.deleteRaceEvent(raceEvent1);
        raceEvents = dao.getRaceEvents();
        Assert.assertTrue(raceEvents.isEmpty());
    }

    @Test
    public void removeRaceEventNotExisting() throws Exception {
        List<RaceEvent> raceEvents = dao.getRaceEvents();
        Assert.assertTrue(raceEvents.isEmpty());
        Race race1 = new Race();
        race1.setId(1L);
        race1.setName("Race 1");
        em.merge(race1);
        Event event1 = new Event();
        event1.setName("Event 1");
        RaceEvent raceEvent1 = new RaceEvent();
        raceEvent1.setId(1L);
        raceEvent1.setRace(race1);
        raceEvent1.setRaceEventOrder(1);
        raceEvent1.setEvent(event1);
        dao.tx = new EntityUserTransaction(em);
        dao.addRaceEvent(raceEvent1);

        raceEvents = dao.getRaceEvents();
        Assert.assertTrue(!raceEvents.isEmpty());
        Assert.assertTrue(raceEvents.size() == 1);

        RaceEvent raceEvent2 = new RaceEvent();
        raceEvent2.setId(2L);
        raceEvent2.setRaceEventOrder(2);
        raceEvent2.setRace(race1);
        raceEvent2.setEvent(event1);

        dao.deleteRaceEvent(raceEvent2);
        Assert.assertTrue(!raceEvents.isEmpty());
        Assert.assertTrue(raceEvents.size() == 1);
        Assert.assertEquals(raceEvents.get(0).getRaceEventOrder(), 1L);

    }

    @Test (expected = DaoException.class)
    public void removeRaceEventWithException() throws Exception {
        List<RaceEvent> raceEvents = dao.getRaceEvents();
        Assert.assertTrue(raceEvents.isEmpty());
        Race race1 = new Race();
        race1.setId(1L);
        race1.setName("Race 1");
        Event event1 = new Event();
        event1.setName("Event 1");
        RaceEvent raceEvent1 = new RaceEvent();
        raceEvent1.setId(1L);
        raceEvent1.setRace(race1);
        raceEvent1.setEvent(event1);
        EntityManager em = new MockUp<EntityManager>(){
            @Mock
            public <T> T find(Class<T> entityClass, Object primaryKey) throws NotSupportedException {
                throw new NotSupportedException();
            }
        }.getMockInstance();
        dao.tx = new EntityUserTransaction(em);
        dao.em = em;
        dao.deleteRaceEvent(raceEvent1);
    }

    @Test
    public void updateRaceEvent() throws Exception {
        List<RaceEvent> raceEvents = dao.getRaceEvents();
        Assert.assertTrue(raceEvents.isEmpty());
        Race race1 = new Race();
        race1.setId(1L);
        race1.setName("Race 1");
        em.merge(race1);
        Event event1 = new Event();
        event1.setName("Event 1");
        RaceEvent raceEvent1 = new RaceEvent();
        raceEvent1.setId(1L);
        raceEvent1.setRace(race1);
        raceEvent1.setEvent(event1);
        raceEvent1.setRaceEventOrder(1);
        dao.tx = new EntityUserTransaction(em);
        dao.addRaceEvent(raceEvent1);

        raceEvents = dao.getRaceEvents();
        Assert.assertTrue(!raceEvents.isEmpty());
        Assert.assertTrue(raceEvents.size() == 1);
        Assert.assertEquals(raceEvents.get(0).getRaceEventOrder(), 1);

        raceEvent1.setRaceEventOrder(2);
        dao.updateRaceEvent(raceEvent1);
        raceEvents = dao.getRaceEvents();
        Assert.assertTrue(!raceEvents.isEmpty());
        Assert.assertTrue(raceEvents.size() == 1);
        Assert.assertEquals(raceEvents.get(0).getRaceEventOrder(), 2);
    }

    @Test (expected = DaoException.class)
    public void updateRaceEventWithException() throws Exception {
        List<RaceEvent> raceEvents = dao.getRaceEvents();
        Assert.assertTrue(raceEvents.isEmpty());
        Race race1 = new Race();
        race1.setId(1L);
        race1.setName("Race 1");
        Event event1 = new Event();
        event1.setId(1L);
        event1.setName("Event 1");
        RaceEvent raceEvent1 = new RaceEvent();
        raceEvent1.setId(1L);
        raceEvent1.setRace(race1);
        raceEvent1.setEvent(event1);
        raceEvent1.setRaceEventOrder(1);
        em.merge(race1);
        em.merge(event1);
        UserTransaction tx = new MockUp<UserTransaction>() {
            @Mock
            void commit() throws HeuristicMixedException, HeuristicRollbackException, IllegalStateException, RollbackException, SecurityException, SystemException {
                throw new HeuristicMixedException();
            }

        }.getMockInstance();
        dao.tx = tx;
        dao.updateRaceEvent(raceEvent1);
    }
}
