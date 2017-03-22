package org.tiatus.dao;

import mockit.Mock;
import mockit.MockUp;
import org.junit.Assert;
import org.junit.Test;
import org.tiatus.entity.Event;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnreynolds on 14/09/2016.
 */
public class EventDaoTest {
    @Test
    public void testAddEvent() throws DaoException {
        TypedQuery typedQuery = new MockUp<TypedQuery>() {
            @Mock
            public List<Event> getResultList() {
                return new ArrayList<>();
            }
        }.getMockInstance();

        EntityManager em = new MockUp<EntityManager>() {
            @Mock
            public Event find(Class entityClass, Object primaryKey) {
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

        EventDaoImpl dao = new EventDaoImpl();
        dao.em = em;
        dao.tx = tx;
        Event event = new Event();
        event.setId(1L);
        dao.addEvent(event);
    }

    @Test (expected = DaoException.class)
    public void testAddEventExisting() throws DaoException {
        EntityManager em = new MockUp<EntityManager>() {
            @Mock
            public Event find(Class entityClass, Object primaryKey) {
                Event event = new Event();
                event.setId(1L);
                return event;
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

        EventDaoImpl dao = new EventDaoImpl();
        dao.em = em;
        dao.tx = tx;
        Event event = new Event();
        event.setId(1L);
        dao.addEvent(event);
    }

    @Test (expected = DaoException.class)
    public void testAddEventException() throws DaoException {
        EntityManager em = new MockUp<EntityManager>() {
            @Mock
            public Event find(Class entityClass, Object primaryKey) {
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

        EventDaoImpl dao = new EventDaoImpl();
        dao.em = em;
        dao.tx = tx;
        Event event = new Event();
        event.setId(1L);
        dao.addEvent(event);
    }

    @Test
    public void testGetEvents() {
        TypedQuery typedQuery = new MockUp<TypedQuery>() {
            @Mock
            public List<Event> getResultList() {
                List<Event> list = new ArrayList<>();
                Event event = new Event();
                list.add(event);
                return list;
            }
        }.getMockInstance();

        EntityManager em = new MockUp<EntityManager>() {
            @Mock
            public TypedQuery createQuery(String qlString, Class resultClass) {
                return typedQuery;
            }
        }.getMockInstance();

        EventDaoImpl dao = new EventDaoImpl();
        dao.em = em;
        Assert.assertFalse(dao.getEvents().isEmpty());
    }

    @Test
    public void testGetUnasignedEvents() {
        TypedQuery typedQuery = new MockUp<TypedQuery>() {
            @Mock
            public List<Event> getResultList() {
                List<Event> list = new ArrayList<>();
                Event event = new Event();
                list.add(event);
                return list;
            }
        }.getMockInstance();

        EntityManager em = new MockUp<EntityManager>() {
            @Mock
            public TypedQuery createQuery(String qlString, Class resultClass) {
                return typedQuery;
            }
        }.getMockInstance();

        EventDaoImpl dao = new EventDaoImpl();
        dao.em = em;
        Assert.assertFalse(dao.getUnassignedEvents().isEmpty());
    }

    @Test
    public void testRemoveEvent() throws Exception {
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
            public Event find(Class<Event> entityClass, Object primaryKey) {
                Event event = new Event();
                event.setId(1L);
                return event;
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

        EventDaoImpl dao = new EventDaoImpl();
        dao.em = em;
        dao.tx = tx;
        Event event = new Event();
        event.setId(1L);
        dao.deleteEvent(event);
    }

    @Test
    public void testRemoveEventNoEvent() throws Exception {
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
            public Event find(Class<Event> entityClass, Object primaryKey) {
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

        EventDaoImpl dao = new EventDaoImpl();
        dao.em = em;
        dao.tx = tx;
        Event event = new Event();
        event.setId(1L);
        dao.deleteEvent(event);
    }

    @Test (expected = DaoException.class)
    public void testRemoveEventException() throws Exception {
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
            public Event find(Class<Event> entityClass, Object primaryKey) {
                Event event = new Event();
                event.setId(1L);
                return event;
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

        EventDaoImpl dao = new EventDaoImpl();
        dao.em = em;
        dao.tx = tx;
        Event event = new Event();
        event.setId(1L);
        dao.deleteEvent(event);
    }

    @Test
    public void testUpdateEvent() throws Exception {
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

        EventDaoImpl dao = new EventDaoImpl();
        dao.em = em;
        dao.tx = tx;
        Event event = new Event();
        event.setId(1L);
        dao.updateEvent(event);
    }

    @Test (expected = DaoException.class)
    public void testUpdateEventException() throws Exception {
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

        EventDaoImpl dao = new EventDaoImpl();
        dao.em = em;
        dao.tx = tx;
        Event event = new Event();
        event.setId(1L);
        dao.updateEvent(event);
    }
}
