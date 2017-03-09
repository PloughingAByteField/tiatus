package org.tiatus.dao;

import mockit.Mock;
import mockit.MockUp;
import org.junit.Assert;
import org.junit.Test;
import org.tiatus.entity.Position;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnreynolds on 14/09/2016.
 */
public class PositionDaoTest {
    @Test
    public void testAddPosition() throws DaoException {
        EntityManager em = new MockUp<EntityManager>() {
            @Mock
            public Position find(Class entityClass, Object primaryKey) {
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

        PositionDaoImpl dao = new PositionDaoImpl();
        dao.em = em;
        dao.tx = tx;
        Position position = new Position();
        position.setId(1L);
        dao.addPosition(position);
    }

    @Test (expected = DaoException.class)
    public void testAddPositionExisting() throws DaoException {
        EntityManager em = new MockUp<EntityManager>() {
            @Mock
            public Position find(Class entityClass, Object primaryKey) {
                Position position = new Position();
                position.setId(1L);
                return position;
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

        PositionDaoImpl dao = new PositionDaoImpl();
        dao.em = em;
        dao.tx = tx;
        Position position = new Position();
        position.setId(1L);
        dao.addPosition(position);
    }

    @Test (expected = DaoException.class)
    public void testAddPositionException() throws DaoException {
        EntityManager em = new MockUp<EntityManager>() {
            @Mock
            public Position find(Class entityClass, Object primaryKey) {
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

        PositionDaoImpl dao = new PositionDaoImpl();
        dao.em = em;
        dao.tx = tx;
        Position position = new Position();
        position.setId(1L);
        dao.addPosition(position);
    }

    @Test
    public void testGetPositions() {
        TypedQuery typedQuery = new MockUp<TypedQuery>() {
            @Mock
            public List<Position> getResultList() {
                List<Position> list = new ArrayList<>();
                Position position = new Position();
                list.add(position);
                return list;
            }
        }.getMockInstance();

        EntityManager em = new MockUp<EntityManager>() {
            @Mock
            public TypedQuery createQuery(String qlString, Class resultClass) {
                return typedQuery;
            }
        }.getMockInstance();

        PositionDaoImpl dao = new PositionDaoImpl();
        dao.em = em;
        Assert.assertFalse(dao.getPositions().isEmpty());
    }

    @Test
    public void testRemovePosition() throws Exception {
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
            public Position find(Class<Position> entityClass, Object primaryKey) {
                Position position = new Position();
                position.setId(1L);
                return position;
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

        PositionDaoImpl dao = new PositionDaoImpl();
        dao.em = em;
        dao.tx = tx;
        Position position = new Position();
        position.setId(1L);
        dao.removePosition(position);
    }

    @Test
    public void testRemovePositionNoPosition() throws Exception {
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
            public Position find(Class<Position> entityClass, Object primaryKey) {
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

        PositionDaoImpl dao = new PositionDaoImpl();
        dao.em = em;
        dao.tx = tx;
        Position position = new Position();
        position.setId(1L);
        dao.removePosition(position);
    }

    @Test (expected = DaoException.class)
    public void testRemovePositionException() throws Exception {
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
            public Position find(Class<Position> entityClass, Object primaryKey) {
                Position position = new Position();
                position.setId(1L);
                return position;
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

        PositionDaoImpl dao = new PositionDaoImpl();
        dao.em = em;
        dao.tx = tx;
        Position position = new Position();
        position.setId(1L);
        dao.removePosition(position);
    }

    @Test
    public void testUpdatePosition() throws Exception {
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

        PositionDaoImpl dao = new PositionDaoImpl();
        dao.em = em;
        dao.tx = tx;
        Position position = new Position();
        position.setId(1L);
        dao.updatePosition(position);
    }

    @Test (expected = DaoException.class)
    public void testUpdatePositionException() throws Exception {
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

        PositionDaoImpl dao = new PositionDaoImpl();
        dao.em = em;
        dao.tx = tx;
        Position position = new Position();
        position.setId(1L);
        dao.updatePosition(position);
    }
}
