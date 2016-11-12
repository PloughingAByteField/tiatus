package org.tiatus.dao;

import mockit.Mock;
import mockit.MockUp;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.entity.Club;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.transaction.*;
import java.util.List;

/**
 * Created by johnreynolds on 19/06/2016.
 */
public class ClubIT {

    private static final Logger LOG = LoggerFactory.getLogger(ClubIT.class);
    private ClubDaoImpl dao;
    private EntityManager em;

    @Before
    public void setUp() throws Exception {
        dao = new ClubDaoImpl();
        em = Persistence.createEntityManagerFactory("primary").createEntityManager();
        dao.em = em;
    }

    @After
    public void tearDown() throws Exception {
        em.close();
    }

    @Test
    public void getClubs() {
        List<Club> clubs = dao.getClubs();
        Assert.assertTrue(clubs.isEmpty());

        // add club
        em.getTransaction().begin();
        Club club1 = new Club();
        club1.setId(1L);
        club1.setClub("Club 1");
        Club club2 = new Club();
        club2.setId(2L);
        club2.setClub("Club 2");
        em.merge(club1);
        em.merge(club2);
        em.getTransaction().commit();

        clubs = dao.getClubs();
        Assert.assertTrue(!clubs.isEmpty());
        Assert.assertTrue(clubs.size() == 2);
    }

    @Test
    public void addClub() throws Exception {
        List<Club> clubs = dao.getClubs();
        Assert.assertTrue(clubs.isEmpty());
        Club newClub = new Club();
        newClub.setId(1L);
        newClub.setClub("Club 1");
        dao.tx = new EntityUserTransaction(em);
        dao.addClub(newClub);

        clubs = dao.getClubs();
        Assert.assertTrue(!clubs.isEmpty());
        Assert.assertTrue(clubs.size() == 1);
    }


    @Test (expected = DaoException.class)
    public void addExistingClub() throws Exception {
        List<Club> clubs = dao.getClubs();
        Assert.assertTrue(clubs.isEmpty());
        Club newClub = new Club();
        newClub.setId(1L);
        newClub.setClub("Club 1");
        dao.tx = new EntityUserTransaction(em);
        dao.addClub(newClub);
        dao.addClub(newClub);
    }

    @Test (expected = DaoException.class)
    public void testDaoExceptionWithException() throws Exception {
        List<Club> clubs = dao.getClubs();
        Assert.assertTrue(clubs.isEmpty());
        Club newClub = new Club();
        newClub.setId(1L);
        newClub.setClub("Club 1");
        EntityManager em = new MockUp<EntityManager>(){
            @Mock
            public <T> T find(Class<T> entityClass, Object primaryKey) throws NotSupportedException {
                throw new NotSupportedException();
            }
        }.getMockInstance();
        dao.em = em;

        dao.addClub(newClub);
    }

    @Test
    public void removeClub() throws Exception {
        List<Club> clubs = dao.getClubs();
        Assert.assertTrue(clubs.isEmpty());
        Club club = new Club();
        club.setId(1L);
        club.setClub("Club 1");
        dao.tx = new EntityUserTransaction(em);
        dao.addClub(club);

        clubs = dao.getClubs();
        Assert.assertTrue(!clubs.isEmpty());
        Assert.assertTrue(clubs.size() == 1);

        dao.removeClub(club);
        clubs = dao.getClubs();
        Assert.assertTrue(clubs.isEmpty());
    }


    @Test
    public void removeClubNotExisting() throws Exception {
        List<Club> clubs = dao.getClubs();
        Assert.assertTrue(clubs.isEmpty());
        Club club = new Club();
        club.setId(1L);
        club.setClub("Club 1");
        dao.tx = new EntityUserTransaction(em);
        dao.addClub(club);

        clubs = dao.getClubs();
        Assert.assertTrue(!clubs.isEmpty());
        Assert.assertTrue(clubs.size() == 1);

        Club club2 = new Club();
        club2.setId(2L);
        club2.setClub("Club 2");
        dao.removeClub(club2);
        clubs = dao.getClubs();
        Assert.assertTrue(!clubs.isEmpty());
        Assert.assertTrue(clubs.size() == 1);
        Assert.assertEquals(clubs.get(0).getClub(), "Club 1");
    }


    @Test (expected = DaoException.class)
    public void removeClubWithException() throws Exception {
        Club club = new Club();
        club.setId(1L);
        club.setClub("Club 1");

        EntityManager em = new MockUp<EntityManager>(){
            @Mock
            public <T> T find(Class<T> entityClass, Object primaryKey) throws NotSupportedException {
                throw new NotSupportedException();
            }
        }.getMockInstance();
        dao.em = em;

        dao.removeClub(club);
    }

    @Test
    public void updateClub() throws Exception {
        List<Club> clubs = dao.getClubs();
        Assert.assertTrue(clubs.isEmpty());
        Club newClub = new Club();
        newClub.setId(1L);
        newClub.setClub("Club 1");
        dao.tx = new EntityUserTransaction(em);
        dao.addClub(newClub);

        clubs = dao.getClubs();
        Assert.assertTrue(!clubs.isEmpty());
        Assert.assertTrue(clubs.size() == 1);
        Assert.assertEquals(clubs.get(0).getClub(), "Club 1");
        newClub.setClub("new name");
        dao.updateClub(newClub);
        clubs = dao.getClubs();
        Assert.assertTrue(!clubs.isEmpty());
        Assert.assertTrue(clubs.size() == 1);
        Assert.assertEquals(clubs.get(0).getId(), Long.valueOf(1L));
        Assert.assertEquals(clubs.get(0).getClub(), "new name");
    }

    @Test (expected = DaoException.class)
    public void updateClubWithException() throws Exception {
        Club club = new Club();
        club.setId(1L);
        club.setClub("Club 1");

        UserTransaction tx = new MockUp<UserTransaction>() {
            @Mock
            void commit() throws HeuristicMixedException, HeuristicRollbackException, IllegalStateException, RollbackException, SecurityException, SystemException {
                throw new HeuristicMixedException();
            }

        }.getMockInstance();
        dao.tx = tx;
        dao.updateClub(club);
    }
}
