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
import org.tiatus.entity.Event;
import org.tiatus.entity.Race;
import org.tiatus.entity.RaceEvent;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

import java.util.List;

/**
 * Created by johnreynolds on 19/06/2016.
 */
@ExtendWith(MockitoExtension.class)
public class RaceEventIT {

    // private static final Logger LOG = LoggerFactory.getLogger(RaceEventIT.class);
    // private RaceEventDaoImpl dao;
    // private EntityManager em;

    // @BeforeEach
    // public void setUp() throws Exception {
    //     dao = new RaceEventDaoImpl();
    //     em = Persistence.createEntityManagerFactory("primary").createEntityManager();
    //     dao.em = em;
    // }

    // @AfterEach
    // public void tearDown() throws Exception {
    //     em.close();
    // }

    // @Test
    // public void getRaceEvents() {
    //     List<RaceEvent> raceEvents = dao.getRaceEvents();
    //     Assertions.assertTrue(raceEvents.isEmpty());

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
    //     Event event1 = new Event();
    //     event1.setId(1L);
    //     event1.setName("Event 1");
    //     em.merge(event1);
    //     Event event2 = new Event();
    //     event2.setId(2L);
    //     event2.setName("Event 2");
    //     em.merge(event2);

    //     RaceEvent raceEvent1 = new RaceEvent();
    //     raceEvent1.setId(1L);
    //     raceEvent1.setRace(race1);
    //     raceEvent1.setEvent(event1);
    //     em.merge(raceEvent1);
    //     RaceEvent raceEvent2 = new RaceEvent();
    //     raceEvent2.setId(2L);
    //     raceEvent2.setRace(race2);
    //     raceEvent2.setEvent(event2);
    //     em.merge(raceEvent2);

    //     em.getTransaction().commit();

    //     raceEvents = dao.getRaceEvents();
    //     Assertions.assertTrue(!raceEvents.isEmpty());
    //     Assertions.assertTrue(raceEvents.size() == 2);
    // }

    // @Test
    // public void addRaceEvent() throws Exception {
    //     List<RaceEvent> raceEvents = dao.getRaceEvents();
    //     Assertions.assertTrue(raceEvents.isEmpty());
    //     Race race1 = new Race();
    //     race1.setId(1L);
    //     race1.setName("Race 1");
    //     em.merge(race1);
    //     Event event1 = new Event();
    //     event1.setName("Event 1");
    //     RaceEvent raceEvent1 = new RaceEvent();
    //     raceEvent1.setRace(race1);
    //     raceEvent1.setEvent(event1);
    //     dao.tx = new EntityUserTransaction(em);
    //     dao.addRaceEvent(raceEvent1);

    //     raceEvents = dao.getRaceEvents();
    //     Assertions.assertTrue(!raceEvents.isEmpty());
    //     Assertions.assertTrue(raceEvents.size() == 1);
    // }


    // @Test
    // public void addExistingRaceEvent() throws Exception {
    //     List<RaceEvent> raceEvents = dao.getRaceEvents();
    //     Assertions.assertTrue(raceEvents.isEmpty());
    //     Race race1 = new Race();
    //     race1.setId(1L);
    //     race1.setName("Race 1");
    //     em.merge(race1);
    //     Event event1 = new Event();
    //     event1.setId(1L);
    //     event1.setName("Event 1");
    //     em.merge(event1);
    //     RaceEvent raceEvent1 = new RaceEvent();
    //     raceEvent1.setId(1L);
    //     raceEvent1.setRace(race1);
    //     raceEvent1.setEvent(event1);
    //     dao.tx = new EntityUserTransaction(em);
    //     dao.addRaceEvent(raceEvent1);

    //     Assertions.assertThrows(DaoException.class, () -> {
    //         dao.addRaceEvent(raceEvent1);
    //     });
    // }

    // @Test
    // public void testAddRaceEventWithException() throws Exception {
    //     List<RaceEvent> raceEvents = dao.getRaceEvents();
    //     Assertions.assertTrue(raceEvents.isEmpty());
    //     Race race1 = new Race();
    //     race1.setId(1L);
    //     race1.setName("Race 1");
    //     em.merge(race1);
    //     Event event1 = new Event();
    //     event1.setName("Event 1");
    //     RaceEvent raceEvent1 = new RaceEvent();
    //     raceEvent1.setId(1L);
    //     raceEvent1.setRace(race1);
    //     raceEvent1.setEvent(event1);

    //     EntityManager em = Mockito.mock(EntityManager.class);
    //     doThrow(NotSupportedException.class).when(em).find(any(), any());
        
    //     dao.tx = new EntityUserTransaction(em);
    //     dao.em = em;

    //     Assertions.assertThrows(DaoException.class, () -> {
    //         dao.addRaceEvent(raceEvent1);
    //     });
    // }

    // @Test
    // public void removeRaceEvent() throws Exception {
    //     List<RaceEvent> raceEvents = dao.getRaceEvents();
    //     Assertions.assertTrue(raceEvents.isEmpty());
    //     Race race1 = new Race();
    //     race1.setId(1L);
    //     race1.setName("Race 1");
    //     em.merge(race1);
    //     Event event1 = new Event();
    //     event1.setName("Event 1");
    //     RaceEvent raceEvent1 = new RaceEvent();
    //     raceEvent1.setRace(race1);
    //     raceEvent1.setEvent(event1);
    //     dao.tx = new EntityUserTransaction(em);
    //     dao.addRaceEvent(raceEvent1);

    //     raceEvents = dao.getRaceEvents();
    //     Assertions.assertTrue(!raceEvents.isEmpty());
    //     Assertions.assertTrue(raceEvents.size() == 1);

    //     dao.deleteRaceEvent(raceEvent1);
    //     raceEvents = dao.getRaceEvents();
    //     Assertions.assertTrue(raceEvents.isEmpty());
    // }

    // @Test
    // public void removeRaceEventNotExisting() throws Exception {
    //     List<RaceEvent> raceEvents = dao.getRaceEvents();
    //     Assertions.assertTrue(raceEvents.isEmpty());
    //     Race race1 = new Race();
    //     race1.setId(1L);
    //     race1.setName("Race 1");
    //     em.merge(race1);
    //     Event event1 = new Event();
    //     event1.setName("Event 1");
    //     RaceEvent raceEvent1 = new RaceEvent();
    //     raceEvent1.setRace(race1);
    //     raceEvent1.setRaceEventOrder(1);
    //     raceEvent1.setEvent(event1);
    //     dao.tx = new EntityUserTransaction(em);
    //     dao.addRaceEvent(raceEvent1);

    //     raceEvents = dao.getRaceEvents();
    //     Assertions.assertTrue(!raceEvents.isEmpty());
    //     Assertions.assertTrue(raceEvents.size() == 1);

    //     RaceEvent raceEvent2 = new RaceEvent();
    //     raceEvent2.setId(2L);
    //     raceEvent2.setRaceEventOrder(2);
    //     raceEvent2.setRace(race1);
    //     raceEvent2.setEvent(event1);

    //     dao.deleteRaceEvent(raceEvent2);
    //     Assertions.assertTrue(!raceEvents.isEmpty());
    //     Assertions.assertTrue(raceEvents.size() == 1);
    //     Assertions.assertEquals(raceEvents.get(0).getRaceEventOrder(), 1L);
    // }

    // @Test
    // public void removeRaceEventWithException() throws Exception {
    //     List<RaceEvent> raceEvents = dao.getRaceEvents();
    //     Assertions.assertTrue(raceEvents.isEmpty());
    //     Race race1 = new Race();
    //     race1.setId(1L);
    //     race1.setName("Race 1");
    //     Event event1 = new Event();
    //     event1.setName("Event 1");
    //     RaceEvent raceEvent1 = new RaceEvent();
    //     raceEvent1.setId(1L);
    //     raceEvent1.setRace(race1);
    //     raceEvent1.setEvent(event1);
        
    //     EntityManager em = Mockito.mock(EntityManager.class);
    //     doThrow(NotSupportedException.class).when(em).find(any(), any());
        
    //     dao.tx = new EntityUserTransaction(em);
    //     dao.em = em;

    //     Assertions.assertThrows(DaoException.class, () -> {
    //         dao.deleteRaceEvent(raceEvent1);
    //     });
    // }

    // @Test
    // public void updateRaceEvent() throws Exception {
    //     List<RaceEvent> raceEvents = dao.getRaceEvents();
    //     Assertions.assertTrue(raceEvents.isEmpty());
    //     Race race1 = new Race();
    //     race1.setId(1L);
    //     race1.setName("Race 1");
    //     em.merge(race1);
    //     Event event1 = new Event();
    //     event1.setName("Event 1");
    //     RaceEvent raceEvent1 = new RaceEvent();
    //     raceEvent1.setRace(race1);
    //     raceEvent1.setEvent(event1);
    //     raceEvent1.setRaceEventOrder(1);
    //     dao.tx = new EntityUserTransaction(em);
    //     dao.addRaceEvent(raceEvent1);

    //     raceEvents = dao.getRaceEvents();
    //     Assertions.assertTrue(!raceEvents.isEmpty());
    //     Assertions.assertTrue(raceEvents.size() == 1);
    //     Assertions.assertEquals(raceEvents.get(0).getRaceEventOrder(), 1);

    //     raceEvent1.setRaceEventOrder(2);
    //     dao.updateRaceEvent(raceEvent1);
    //     raceEvents = dao.getRaceEvents();
    //     Assertions.assertTrue(!raceEvents.isEmpty());
    //     Assertions.assertTrue(raceEvents.size() == 1);
    //     Assertions.assertEquals(raceEvents.get(0).getRaceEventOrder(), 2);
    // }

    // @Test
    // public void updateRaceEventWithException() throws Exception {
    //     List<RaceEvent> raceEvents = dao.getRaceEvents();
    //     Assertions.assertTrue(raceEvents.isEmpty());
    //     Race race1 = new Race();
    //     race1.setId(1L);
    //     race1.setName("Race 1");
    //     Event event1 = new Event();
    //     event1.setId(1L);
    //     event1.setName("Event 1");
    //     RaceEvent raceEvent1 = new RaceEvent();
    //     raceEvent1.setId(1L);
    //     raceEvent1.setRace(race1);
    //     raceEvent1.setEvent(event1);
    //     raceEvent1.setRaceEventOrder(1);
    //     em.merge(race1);
    //     em.merge(event1);
       
    //     UserTransaction userTransactionMock = Mockito.mock(UserTransaction.class);
    //     doThrow(HeuristicMixedException.class).when(userTransactionMock).commit();
        
    //     dao.tx = userTransactionMock;

    //     Assertions.assertThrows(DaoException.class, () -> {
    //         dao.updateRaceEvent(raceEvent1);
    //     });
    // }
}
