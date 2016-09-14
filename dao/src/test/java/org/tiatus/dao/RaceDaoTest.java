package org.tiatus.dao;

import mockit.Mock;
import mockit.MockUp;
import org.junit.Assert;
import org.junit.Test;
import org.tiatus.entity.Race;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnreynolds on 14/09/2016.
 */
public class RaceDaoTest {
    @Test
    public void testAddRace() throws DaoException {
        EntityManager em = new MockUp<EntityManager>() {
            @Mock
            public Race find(Class entityClass, Object primaryKey) {
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

            }

        }.getMockInstance();

        RaceDaoImpl dao = new RaceDaoImpl();
        dao.em = em;
        dao.tx = tx;
        Race race = new Race();
        race.setId(1L);
        dao.addRace(race);
    }

    @Test (expected = DaoException.class)
    public void testAddRaceExisting() throws DaoException {
        EntityManager em = new MockUp<EntityManager>() {
            @Mock
            public Race find(Class entityClass, Object primaryKey) {
                Race race = new Race();
                race.setId(1L);
                return race;
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

        RaceDaoImpl dao = new RaceDaoImpl();
        dao.em = em;
        dao.tx = tx;
        Race race = new Race();
        race.setId(1L);
        dao.addRace(race);
    }

    @Test (expected = DaoException.class)
    public void testAddRaceException() throws DaoException {
        EntityManager em = new MockUp<EntityManager>() {
            @Mock
            public Race find(Class entityClass, Object primaryKey) {
                Race race = new Race();
                race.setId(1L);
                return race;
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

        RaceDaoImpl dao = new RaceDaoImpl();
        dao.em = em;
        dao.tx = tx;
        Race race = new Race();
        race.setId(1L);
        dao.addRace(race);
    }

    @Test
    public void testGetRaces() {
        TypedQuery typedQuery = new MockUp<TypedQuery>() {
            @Mock
            public List<Race> getResultList() {
                List<Race> list = new ArrayList<>();
                Race race = new Race();
                list.add(race);
                return list;
            }
        }.getMockInstance();

        EntityManager em = new MockUp<EntityManager>() {
            @Mock
            public TypedQuery createQuery(String qlString, Class resultClass) {
                return typedQuery;
            }
        }.getMockInstance();

        RaceDaoImpl dao = new RaceDaoImpl();
        dao.em = em;
        Assert.assertFalse(dao.getRaces().isEmpty());
    }

    @Test
    public void testRemoveRace() throws Exception {
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
            public boolean contains(Object entity) {
                return false;
            }

            @Mock
            public <T> T merge(T entity) {
                return entity;
            }
        }.getMockInstance();

        RaceDaoImpl dao = new RaceDaoImpl();
        dao.em = em;
        dao.tx = tx;
        Race race = new Race();
        race.setId(1L);
        dao.removeRace(race);
    }

    @Test (expected = DaoException.class)
    public void testRemoveRaceException() throws Exception {
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
            public boolean contains(Object entity) {
                return false;
            }

            @Mock
            public <T> T merge(T entity) {
                return entity;
            }
        }.getMockInstance();

        RaceDaoImpl dao = new RaceDaoImpl();
        dao.em = em;
        dao.tx = tx;
        Race race = new Race();
        race.setId(1L);
        dao.removeRace(race);
    }

    @Test
    public void testUpdateRace() throws Exception {
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

        RaceDaoImpl dao = new RaceDaoImpl();
        dao.em = em;
        dao.tx = tx;
        Race race = new Race();
        race.setId(1L);
        dao.updateRace(race);
    }

    @Test (expected = DaoException.class)
    public void testUpdateRaceException() throws Exception {
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

        RaceDaoImpl dao = new RaceDaoImpl();
        dao.em = em;
        dao.tx = tx;
        Race race = new Race();
        race.setId(1L);
        dao.updateRace(race);
    }
}
