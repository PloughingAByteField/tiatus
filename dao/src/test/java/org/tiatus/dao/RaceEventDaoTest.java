package org.tiatus.dao;

import mockit.Mock;
import mockit.MockUp;
import org.junit.Assert;
import org.junit.Test;
import org.tiatus.entity.Event;
import org.tiatus.entity.Race;
import org.tiatus.entity.RaceEvent;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnreynolds on 14/09/2016.
 */
public class RaceEventDaoTest {
    @Test
    public void testAddRace() throws DaoException {
        TypedQuery typedQuery = new MockUp<TypedQuery>() {
            @Mock
            public List<RaceEvent> getResultList() {
                return new ArrayList<>();
            }

            @Mock
            public TypedQuery setParameter(String name, Object value) {
                return this.getMockInstance();
            }
        }.getMockInstance();

        EntityManager em = new MockUp<EntityManager>() {
            @Mock
            public RaceEvent find(Class entityClass, Object primaryKey) {
                return null;
            }

            @Mock
            public void persist(Object entity) {

            }

            @Mock
            public TypedQuery createQuery(String qlString, Class resultClass) {
                return typedQuery;
            }
        }.getMockInstance();

        UserTransaction tx = new MockUp<UserTransaction>() {
            @Mock
            void begin() throws NotSupportedException, SystemException {

            }

            @Mock
            void commit() throws HeuristicMixedException, HeuristicRollbackException, IllegalStateException, RollbackException, SecurityException, SystemException {

            }

        }.getMockInstance();

        RaceEventDaoImpl dao = new RaceEventDaoImpl();
        dao.em = em;
        dao.tx = tx;
        RaceEvent raceEvent = new RaceEvent();
        Race race = new Race();
        race.setId(1L);
        Event event = new Event();
        event.setId(1L);
        raceEvent.setId(1L);
        raceEvent.setEvent(event);
        raceEvent.setRace(race);
        dao.addRaceEvent(raceEvent);
    }

    @Test (expected = DaoException.class)
    public void testAddRaceEventExisting() throws DaoException {
        EntityManager em = new MockUp<EntityManager>() {
            @Mock
            public RaceEvent find(Class entityClass, Object primaryKey) {
                RaceEvent RaceEvent = new RaceEvent();
                RaceEvent.setId(1L);
                return RaceEvent;
            }

            @Mock
            public void persist(Object entity) {

            }
        }.getMockInstance();

        UserTransaction tx = new MockUp<UserTransaction>() {
            @Mock
            void begin() throws NotSupportedException, SystemException {

            }

            @Mock
            void commit() throws HeuristicMixedException, HeuristicRollbackException, IllegalStateException, RollbackException, SecurityException, SystemException {

            }

        }.getMockInstance();

        RaceEventDaoImpl dao = new RaceEventDaoImpl();
        dao.em = em;
        dao.tx = tx;
        RaceEvent raceEvent = new RaceEvent();
        Race race = new Race();
        race.setId(1L);
        Event event = new Event();
        event.setId(1L);
        raceEvent.setId(1L);
        raceEvent.setEvent(event);
        raceEvent.setRace(race);
        dao.addRaceEvent(raceEvent);
    }

    @Test (expected = DaoException.class)
    public void testAddRaceEventException() throws DaoException {
        EntityManager em = new MockUp<EntityManager>() {
            @Mock
            public RaceEvent find(Class entityClass, Object primaryKey) {
                return null;
            }

            @Mock
            public void persist(Object entity) {

            }
        }.getMockInstance();

        UserTransaction tx = new MockUp<UserTransaction>() {
            @Mock
            void begin() throws NotSupportedException, SystemException {

            }

            @Mock
            void commit() throws HeuristicMixedException, HeuristicRollbackException, IllegalStateException, RollbackException, SecurityException, SystemException {
                throw new HeuristicMixedException();
            }

        }.getMockInstance();

        RaceEventDaoImpl dao = new RaceEventDaoImpl();
        dao.em = em;
        dao.tx = tx;
        RaceEvent raceEvent = new RaceEvent();
        Race race = new Race();
        race.setId(1L);
        Event event = new Event();
        event.setId(1L);
        raceEvent.setId(1L);
        raceEvent.setEvent(event);
        raceEvent.setRace(race);
        dao.addRaceEvent(raceEvent);
    }

    private List<RaceEvent> getRaceEvents() {
        List<RaceEvent> list = new ArrayList<>();
        RaceEvent raceEvent = new RaceEvent();
        Race race = new Race();
        race.setId(1L);
        Event event = new Event();
        event.setId(1L);
        raceEvent.setId(1L);
        raceEvent.setEvent(event);
        raceEvent.setRace(race);
        list.add(raceEvent);
        return list;
    }

    @Test
    public void testGetRaceEvents() {
        TypedQuery typedQuery = new MockUp<TypedQuery>() {
            @Mock
            public List<RaceEvent> getResultList() {
               return getRaceEvents();
            }
        }.getMockInstance();

        EntityManager em = new MockUp<EntityManager>() {
            @Mock
            public TypedQuery createQuery(String qlString, Class resultClass) {
                return typedQuery;
            }
        }.getMockInstance();

        RaceEventDaoImpl dao = new RaceEventDaoImpl();
        dao.em = em;
        Assert.assertFalse(dao.getRaceEvents().isEmpty());
    }

    @Test
    public void testGetRaceEventByEvent() {
        TypedQuery typedQuery = new MockUp<TypedQuery>() {
            @Mock
            public List<RaceEvent> getResultList() {
                return getRaceEvents();
            }

            @Mock
            public TypedQuery setParameter(String name, Object value) {
                return this.getMockInstance();
            }
        }.getMockInstance();

        EntityManager em = new MockUp<EntityManager>() {
            @Mock
            public TypedQuery createQuery(String qlString, Class resultClass) {
                return typedQuery;
            }
        }.getMockInstance();

        RaceEventDaoImpl dao = new RaceEventDaoImpl();
        dao.em = em;
        dao.getRaceEventByEvent(getRaceEvents().get(0).getEvent());
    }

    @Test
    public void testRemoveRaceEvent() throws Exception {
        UserTransaction tx = new MockUp<UserTransaction>() {
            @Mock
            void begin() throws NotSupportedException, SystemException {

            }

            @Mock
            void commit() throws HeuristicMixedException, HeuristicRollbackException, IllegalStateException, RollbackException, SecurityException, SystemException {

            }

        }.getMockInstance();

        EntityManager em = new MockUp<EntityManager>() {
            @Mock
            public Object find(Class<?> entityClass, Object primaryKey) {
                if (entityClass.isAssignableFrom(RaceEvent.class)) {
                    RaceEvent raceEvent = new RaceEvent();
                    Race race = new Race();
                    race.setId(1L);
                    Event event = new Event();
                    event.setId(1L);
                    raceEvent.setId(1L);
                    raceEvent.setEvent(event);
                    raceEvent.setRace(race);
                    return raceEvent;
                }
                if (entityClass.isAssignableFrom(Event.class)) {
                    Event event = new Event();
                    event.setId(1L);
                    return event;
                }
                return null;
            }

            @Mock
            public boolean contains(Object entity) {
                return false;
            }

            @Mock
            public <T> T merge(T entity) {
                return entity;
            }
        }.getMockInstance();

        RaceEventDaoImpl dao = new RaceEventDaoImpl();
        dao.em = em;
        dao.tx = tx;
        RaceEvent raceEvent = new RaceEvent();
        Race race = new Race();
        race.setId(1L);
        Event event = new Event();
        event.setId(1L);
        raceEvent.setId(1L);
        raceEvent.setEvent(event);
        raceEvent.setRace(race);
        dao.deleteRaceEvent(raceEvent);
    }

    @Test
    public void testRemoveRaceEventNoRaceEvent() throws Exception {
        UserTransaction tx = new MockUp<UserTransaction>() {
            @Mock
            void begin() throws NotSupportedException, SystemException {

            }

            @Mock
            void commit() throws HeuristicMixedException, HeuristicRollbackException, IllegalStateException, RollbackException, SecurityException, SystemException {

            }

        }.getMockInstance();

        EntityManager em = new MockUp<EntityManager>() {
            @Mock
            public RaceEvent find(Class<Race> entityClass, Object primaryKey) {
                return null;
            }

            @Mock
            public boolean contains(Object entity) {
                return false;
            }

            @Mock
            public <T> T merge(T entity) {
                return entity;
            }
        }.getMockInstance();

        RaceEventDaoImpl dao = new RaceEventDaoImpl();
        dao.em = em;
        dao.tx = tx;
        RaceEvent raceEvent = new RaceEvent();
        Race race = new Race();
        race.setId(1L);
        Event event = new Event();
        event.setId(1L);
        raceEvent.setId(1L);
        raceEvent.setEvent(event);
        raceEvent.setRace(race);
        dao.deleteRaceEvent(raceEvent);
    }

    @Test (expected = DaoException.class)
    public void testRemoveRaceEventException() throws Exception {
        UserTransaction tx = new MockUp<UserTransaction>() {
            @Mock
            void begin() throws NotSupportedException, SystemException {

            }

            @Mock
            void commit() throws HeuristicMixedException, HeuristicRollbackException, IllegalStateException, RollbackException, SecurityException, SystemException {
                throw new HeuristicMixedException();
            }

        }.getMockInstance();

        EntityManager em = new MockUp<EntityManager>() {
            @Mock
            public RaceEvent find(Class<Race> entityClass, Object primaryKey) {
                RaceEvent raceEvent = new RaceEvent();
                Race race = new Race();
                race.setId(1L);
                Event event = new Event();
                event.setId(1L);
                raceEvent.setId(1L);
                raceEvent.setEvent(event);
                raceEvent.setRace(race);
                return raceEvent;
            }

            @Mock
            public boolean contains(Object entity) {
                return false;
            }

            @Mock
            public <T> T merge(T entity) {
                return entity;
            }
        }.getMockInstance();

        RaceEventDaoImpl dao = new RaceEventDaoImpl();
        dao.em = em;
        dao.tx = tx;
        RaceEvent raceEvent = new RaceEvent();
        Race race = new Race();
        race.setId(1L);
        Event event = new Event();
        event.setId(1L);
        raceEvent.setId(1L);
        raceEvent.setEvent(event);
        raceEvent.setRace(race);
        dao.deleteRaceEvent(raceEvent);
    }

    @Test
    public void testUpdateRaceEvent() throws Exception {
        UserTransaction tx = new MockUp<UserTransaction>() {
            @Mock
            void begin() throws NotSupportedException, SystemException {

            }

            @Mock
            void commit() throws HeuristicMixedException, HeuristicRollbackException, IllegalStateException, RollbackException, SecurityException, SystemException {

            }

        }.getMockInstance();

        EntityManager em = new MockUp<EntityManager>() {
            @Mock
            public <T> T merge(T entity) {
                return entity;
            }
        }.getMockInstance();

        RaceEventDaoImpl dao = new RaceEventDaoImpl();
        dao.em = em;
        dao.tx = tx;
        RaceEvent raceEvent = new RaceEvent();
        Race race = new Race();
        race.setId(1L);
        Event event = new Event();
        event.setId(1L);
        raceEvent.setId(1L);
        raceEvent.setEvent(event);
        raceEvent.setRace(race);
        dao.updateRaceEvent(raceEvent);
    }

    @Test (expected = DaoException.class)
    public void testUpdateRaceEventException() throws Exception {
        UserTransaction tx = new MockUp<UserTransaction>() {
            @Mock
            void begin() throws NotSupportedException, SystemException {

            }

            @Mock
            void commit() throws HeuristicMixedException, HeuristicRollbackException, IllegalStateException, RollbackException, SecurityException, SystemException {
                throw new HeuristicMixedException();
            }

        }.getMockInstance();

        EntityManager em = new MockUp<EntityManager>() {
            @Mock
            public <T> T merge(T entity) {
                return entity;
            }
        }.getMockInstance();

        RaceEventDaoImpl dao = new RaceEventDaoImpl();
        dao.em = em;
        dao.tx = tx;
        RaceEvent raceEvent = new RaceEvent();
        Race race = new Race();
        race.setId(1L);
        Event event = new Event();
        event.setId(1L);
        raceEvent.setId(1L);
        raceEvent.setEvent(event);
        raceEvent.setRace(race);
        dao.updateRaceEvent(raceEvent);
    }
}
