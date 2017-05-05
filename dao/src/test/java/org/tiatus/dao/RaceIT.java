package org.tiatus.dao;

import mockit.Mock;
import mockit.MockUp;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.entity.Race;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.transaction.*;
import java.util.List;

/**
 * Created by johnreynolds on 19/06/2016.
 */
public class RaceIT {

    private static final Logger LOG = LoggerFactory.getLogger(RaceIT.class);
    private RaceDaoImpl dao;
    private EntityManager em;

    @Before
    public void setUp() throws Exception {
        dao = new RaceDaoImpl();
        em = Persistence.createEntityManagerFactory("primary").createEntityManager();
        dao.em = em;
    }

    @After
    public void tearDown() throws Exception {
        em.close();
    }

    @Test
    public void getRaces() {
        List<Race> races = dao.getRaces();
        Assert.assertTrue(races.isEmpty());

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
        em.getTransaction().commit();

        races = dao.getRaces();
        Assert.assertTrue(!races.isEmpty());
        Assert.assertTrue(races.size() == 2);
    }

    @Test
    public void addRace() throws Exception {
        List<Race> races = dao.getRaces();
        Assert.assertTrue(races.isEmpty());
        Race newRace = new Race();
        newRace.setName("Race 1");
        dao.tx = new EntityUserTransaction(em);
        dao.addRace(newRace);

        races = dao.getRaces();
        Assert.assertTrue(!races.isEmpty());
        Assert.assertTrue(races.size() == 1);
    }


    @Test (expected = DaoException.class)
    public void addExistingRace() throws Exception {
        List<Race> races = dao.getRaces();
        Assert.assertTrue(races.isEmpty());
        Race newRace = new Race();
        newRace.setName("Race 1");
        dao.tx = new EntityUserTransaction(em);
        dao.addRace(newRace);
        dao.addRace(newRace);
    }

    @Test (expected = DaoException.class)
    public void testDaoExceptionWithException() throws Exception {
        List<Race> races = dao.getRaces();
        Assert.assertTrue(races.isEmpty());
        Race newRace = new Race();
        newRace.setName("Race 1");
        EntityManager em = new MockUp<EntityManager>(){
            @Mock
            public <T> T find(Class<T> entityClass, Object primaryKey) throws NotSupportedException {
                throw new NotSupportedException();
            }
        }.getMockInstance();
        dao.em = em;

        dao.addRace(newRace);
    }

    @Test
    public void removeRace() throws Exception {
        List<Race> races = dao.getRaces();
        Assert.assertTrue(races.isEmpty());
        Race race = new Race();
        race.setName("Race 1");
        dao.tx = new EntityUserTransaction(em);
        dao.addRace(race);

        races = dao.getRaces();
        Assert.assertTrue(!races.isEmpty());
        Assert.assertTrue(races.size() == 1);

        dao.removeRace(race);
        races = dao.getRaces();
        Assert.assertTrue(races.isEmpty());
    }


    @Test
    public void removeRaceNotExisting() throws Exception {
        List<Race> races = dao.getRaces();
        Assert.assertTrue(races.isEmpty());
        Race race = new Race();
        race.setName("Race 1");
        dao.tx = new EntityUserTransaction(em);
        dao.addRace(race);

        races = dao.getRaces();
        Assert.assertTrue(!races.isEmpty());
        Assert.assertTrue(races.size() == 1);

        Race race2 = new Race();
        race2.setName("Race 2");
        dao.removeRace(race2);
        races = dao.getRaces();
        Assert.assertTrue(!races.isEmpty());
        Assert.assertTrue(races.size() == 1);
        Assert.assertEquals(races.get(0).getName(), "Race 1");
    }


    @Test (expected = DaoException.class)
    public void removeRaceWithException() throws Exception {
        Race race = new Race();
        race.setName("Race 1");

        EntityManager em = new MockUp<EntityManager>(){
            @Mock
            public <T> T find(Class<T> entityClass, Object primaryKey) throws NotSupportedException {
                throw new NotSupportedException();
            }
        }.getMockInstance();
        dao.em = em;

        dao.removeRace(race);
    }

    @Test
    public void updateRace() throws Exception {
        List<Race> races = dao.getRaces();
        Assert.assertTrue(races.isEmpty());
        Race newRace = new Race();
        newRace.setName("Race 1");
        dao.tx = new EntityUserTransaction(em);
        dao.addRace(newRace);

        races = dao.getRaces();
        Assert.assertTrue(!races.isEmpty());
        Assert.assertTrue(races.size() == 1);
        Assert.assertEquals(races.get(0).getName(), "Race 1");
        newRace.setName("new name");
        dao.updateRace(newRace);
        races = dao.getRaces();
        Assert.assertTrue(!races.isEmpty());
        Assert.assertTrue(races.size() == 1);
        Assert.assertEquals(races.get(0).getId(), Long.valueOf(1L));
        Assert.assertEquals(races.get(0).getName(), "new name");
    }

    @Test (expected = DaoException.class)
    public void updateRaceWithException() throws Exception {
        Race race = new Race();
        race.setId(1L);
        race.setName("Race 1");

        UserTransaction tx = new MockUp<UserTransaction>() {
            @Mock
            void commit() throws HeuristicMixedException, HeuristicRollbackException, IllegalStateException, RollbackException, SecurityException, SystemException {
                throw new HeuristicMixedException();
            }

        }.getMockInstance();
        dao.tx = tx;
        dao.updateRace(race);
    }
}
