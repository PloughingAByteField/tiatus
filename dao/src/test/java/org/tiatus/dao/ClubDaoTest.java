package org.tiatus.dao;

import mockit.Mock;
import mockit.MockUp;
import org.junit.Assert;
import org.junit.Test;
import org.tiatus.entity.Club;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnreynolds on 14/09/2016.
 */
public class ClubDaoTest {
    @Test
    public void testAddClub() throws DaoException {
        EntityManager em = new MockUp<EntityManager>() {
            @Mock
            public Club find(Class entityClass, Object primaryKey) {
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

        ClubDaoImpl dao = new ClubDaoImpl();
        dao.em = em;
        dao.tx = tx;
        Club club = new Club();
        club.setId(1L);
        dao.addClub(club);
    }

    @Test (expected = DaoException.class)
    public void testAddClubExisting() throws DaoException {
        EntityManager em = new MockUp<EntityManager>() {
            @Mock
            public Club find(Class entityClass, Object primaryKey) {
                Club club = new Club();
                club.setId(1L);
                return club;
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

        ClubDaoImpl dao = new ClubDaoImpl();
        dao.em = em;
        dao.tx = tx;
        Club club = new Club();
        club.setId(1L);
        dao.addClub(club);
    }

    @Test (expected = DaoException.class)
    public void testAddClubException() throws DaoException {
        EntityManager em = new MockUp<EntityManager>() {
            @Mock
            public Club find(Class entityClass, Object primaryKey) {
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

        ClubDaoImpl dao = new ClubDaoImpl();
        dao.em = em;
        dao.tx = tx;
        Club club = new Club();
        club.setId(1L);
        dao.addClub(club);
    }

    @Test
    public void testGetClubs() {
        TypedQuery typedQuery = new MockUp<TypedQuery>() {
            @Mock
            public List<Club> getResultList() {
                List<Club> list = new ArrayList<>();
                Club club = new Club();
                list.add(club);
                return list;
            }
        }.getMockInstance();

        EntityManager em = new MockUp<EntityManager>() {
            @Mock
            public TypedQuery createQuery(String qlString, Class resultClass) {
                return typedQuery;
            }
        }.getMockInstance();

        ClubDaoImpl dao = new ClubDaoImpl();
        dao.em = em;
        Assert.assertFalse(dao.getClubs().isEmpty());
    }

    @Test
    public void testRemoveClub() throws Exception {
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
            public Club find(Class<Club> entityClass, Object primaryKey) {
                Club club = new Club();
                club.setId(1L);
                return club;
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

        ClubDaoImpl dao = new ClubDaoImpl();
        dao.em = em;
        dao.tx = tx;
        Club club = new Club();
        club.setId(1L);
        dao.removeClub(club);
    }

    @Test
    public void testRemoveClubNoClub() throws Exception {
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
            public Club find(Class<Club> entityClass, Object primaryKey) {
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

        ClubDaoImpl dao = new ClubDaoImpl();
        dao.em = em;
        dao.tx = tx;
        Club club = new Club();
        club.setId(1L);
        dao.removeClub(club);
    }

    @Test (expected = DaoException.class)
    public void testRemoveClubException() throws Exception {
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
            public Club find(Class<Club> entityClass, Object primaryKey) {
                Club club = new Club();
                club.setId(1L);
                return club;
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

        ClubDaoImpl dao = new ClubDaoImpl();
        dao.em = em;
        dao.tx = tx;
        Club club = new Club();
        club.setId(1L);
        dao.removeClub(club);
    }

    @Test
    public void testUpdateClub() throws Exception {
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

        ClubDaoImpl dao = new ClubDaoImpl();
        dao.em = em;
        dao.tx = tx;
        Club club = new Club();
        club.setId(1L);
        dao.updateClub(club);
    }

    @Test (expected = DaoException.class)
    public void testUpdateClubException() throws Exception {
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

        ClubDaoImpl dao = new ClubDaoImpl();
        dao.em = em;
        dao.tx = tx;
        Club club = new Club();
        club.setId(1L);
        dao.updateClub(club);
    }
}
