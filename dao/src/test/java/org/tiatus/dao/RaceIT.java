package org.tiatus.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.entity.Race;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

import java.util.List;

/**
 * Created by johnreynolds on 19/06/2016.
 */
@ExtendWith(MockitoExtension.class)
public class RaceIT {

    // private static final Logger LOG = LoggerFactory.getLogger(RaceIT.class);
    // private RaceDaoImpl dao;
    // private EntityManager em;

    // @BeforeEach
    // public void setUp() throws Exception {
    //     dao = new RaceDaoImpl();
    //     em = Persistence.createEntityManagerFactory("primary").createEntityManager();
    //     dao.em = em;
    // }

    // @AfterEach
    // public void tearDown() throws Exception {
    //     em.close();
    // }

    // @Test
    // public void getRaces() {
    //     List<Race> races = dao.getRaces();
    //     Assertions.assertTrue(races.isEmpty());

    //     // add race
    //     em.getTransaction().begin();
    //     Race race1 = new Race();
    //     race1.setId(1L);
    //     race1.setName("Race 1");
    //     Race race2 = new Race();
    //     race2.setId(2L);
    //     race2.setName("Race 2");
    //     em.merge(race1);
    //     em.merge(race2);
    //     em.getTransaction().commit();

    //     races = dao.getRaces();
    //     Assertions.assertTrue(!races.isEmpty());
    //     Assertions.assertTrue(races.size() == 2);
    // }

    // @Test
    // public void addRace() throws Exception {
    //     List<Race> races = dao.getRaces();
    //     Assertions.assertTrue(races.isEmpty());
    //     Race newRace = new Race();
    //     newRace.setName("Race 1");
    //     dao.tx = new EntityUserTransaction(em);
    //     dao.addRace(newRace);

    //     races = dao.getRaces();
    //     Assertions.assertTrue(!races.isEmpty());
    //     Assertions.assertTrue(races.size() == 1);
    // }


    // @Test
    // public void addExistingRace() throws Exception {
    //     List<Race> races = dao.getRaces();
    //     Assertions.assertTrue(races.isEmpty());
    //     Race newRace = new Race();
    //     newRace.setName("Race 1");
    //     dao.tx = new EntityUserTransaction(em);
    //     dao.addRace(newRace);

    //     Assertions.assertThrows(DaoException.class, () -> {
    //         dao.addRace(newRace);
    //     });
    // }

    // @Test 
    // public void testDaoExceptionWithException() throws Exception {
    //     List<Race> races = dao.getRaces();
    //     Assertions.assertTrue(races.isEmpty());
    //     Race newRace = new Race();
    //     newRace.setName("Race 1");
         
    //     EntityManager em = Mockito.mock(EntityManager.class);
    //     doThrow(NotSupportedException.class).when(em).find(any(), any());
        
    //     dao.em = em;

    //     Assertions.assertThrows(DaoException.class, () -> {
    //         dao.addRace(newRace);
    //     });
    // }

    // @Test
    // public void removeRace() throws Exception {
    //     List<Race> races = dao.getRaces();
    //     Assertions.assertTrue(races.isEmpty());
    //     Race race = new Race();
    //     race.setName("Race 1");
    //     dao.tx = new EntityUserTransaction(em);
    //     dao.addRace(race);

    //     races = dao.getRaces();
    //     Assertions.assertTrue(!races.isEmpty());
    //     Assertions.assertTrue(races.size() == 1);

    //     dao.removeRace(race);
    //     races = dao.getRaces();
    //     Assertions.assertTrue(races.isEmpty());
    // }


    // @Test
    // public void removeRaceNotExisting() throws Exception {
    //     List<Race> races = dao.getRaces();
    //     Assertions.assertTrue(races.isEmpty());
    //     Race race = new Race();
    //     race.setName("Race 1");
    //     dao.tx = new EntityUserTransaction(em);
    //     dao.addRace(race);

    //     races = dao.getRaces();
    //     Assertions.assertTrue(!races.isEmpty());
    //     Assertions.assertTrue(races.size() == 1);

    //     Race race2 = new Race();
    //     race2.setName("Race 2");
    //     dao.removeRace(race2);
    //     races = dao.getRaces();
    //     Assertions.assertTrue(!races.isEmpty());
    //     Assertions.assertTrue(races.size() == 1);
    //     Assertions.assertEquals(races.get(0).getName(), "Race 1");
    // }


    // @Test
    // public void removeRaceWithException() throws Exception {
    //     Race race = new Race();
    //     race.setName("Race 1");

    //     EntityManager em = Mockito.mock(EntityManager.class);
    //     doThrow(NotSupportedException.class).when(em).find(any(), any());
        
    //     dao.em = em;

    //     Assertions.assertThrows(DaoException.class, () -> {
    //         dao.removeRace(race);
    //     });
    // }

    // @Test
    // public void updateRace() throws Exception {
    //     List<Race> races = dao.getRaces();
    //     Assertions.assertTrue(races.isEmpty());
    //     Race newRace = new Race();
    //     newRace.setName("Race 1");
    //     dao.tx = new EntityUserTransaction(em);
    //     dao.addRace(newRace);

    //     races = dao.getRaces();
    //     Assertions.assertTrue(!races.isEmpty());
    //     Assertions.assertTrue(races.size() == 1);
    //     Assertions.assertEquals(races.get(0).getName(), "Race 1");
    //     newRace.setName("new name");
    //     dao.updateRace(newRace);
    //     races = dao.getRaces();
    //     Assertions.assertTrue(!races.isEmpty());
    //     Assertions.assertTrue(races.size() == 1);
    //     Assertions.assertEquals(races.get(0).getId(), Long.valueOf(1L));
    //     Assertions.assertEquals(races.get(0).getName(), "new name");
    // }

    // @Test
    // public void updateRaceWithException() throws Exception {
    //     Race race = new Race();
    //     race.setId(1L);
    //     race.setName("Race 1");

    //     UserTransaction userTransactionMock = Mockito.mock(UserTransaction.class);
    //     doThrow(HeuristicMixedException.class).when(userTransactionMock).commit();
        
    //     dao.tx = userTransactionMock;

    //     Assertions.assertThrows(DaoException.class, () -> {
    //         dao.updateRace(race);
    //     });
    // }
}
