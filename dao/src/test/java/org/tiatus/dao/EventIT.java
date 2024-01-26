package org.tiatus.dao;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
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
public class EventIT {

    // private static final Logger LOG = LoggerFactory.getLogger(EventIT.class);
    // private EventDaoImpl dao;
    // private EntityManager em;

    // @BeforeEach
    // public void setUp() throws Exception {
    //     dao = new EventDaoImpl();
    //     em = Persistence.createEntityManagerFactory("primary").createEntityManager();
    //     dao.em = em;
    // }

    // @AfterEach
    // public void tearDown() throws Exception {
    //     em.close();
    // }

    // @Test
    // public void getEvents() {
    //     List<Event> events = dao.getEvents();
    //     Assertions.assertTrue(events.isEmpty());

    //     em.getTransaction().begin();
    //     Event event1 = new Event();
    //     event1.setId(1L);
    //     event1.setName("Event 1");
    //     em.merge(event1);
    //     Event event2 = new Event();
    //     event2.setId(2L);
    //     event2.setName("Event 2");
    //     em.merge(event2);
    //     em.getTransaction().commit();

    //     events = dao.getEvents();
    //     Assertions.assertTrue(!events.isEmpty());
    //     Assertions.assertTrue(events.size() == 2);
    // }

    // @Test
    // public void getUnassignedEvents() {
    //     List<Event> events = dao.getEvents();
    //     Assertions.assertTrue(events.isEmpty());
    //     List<Event> unassignedEvents = dao.getUnassignedEvents();
    //     Assertions.assertTrue(unassignedEvents.isEmpty());

    //     em.getTransaction().begin();
    //     Event event1 = new Event();
    //     event1.setId(1L);
    //     event1.setName("Event 1");
    //     em.merge(event1);
    //     Event event2 = new Event();
    //     event2.setId(2L);
    //     event2.setName("Event 2");
    //     em.merge(event2);
    //     Race race1 = new Race();
    //     race1.setId(1L);
    //     race1.setName("Race 1");
    //     em.merge(race1);
    //     Event event3 = new Event();
    //     event3.setId(3L);
    //     event3.setName("Event 3");
    //     em.merge(event3);
    //     RaceEvent raceEvent1 = new RaceEvent();
    //     raceEvent1.setId(1L);
    //     raceEvent1.setRace(race1);
    //     raceEvent1.setEvent(event3);
    //     raceEvent1.setRaceEventOrder(1);
    //     em.merge(raceEvent1);
    //     em.getTransaction().commit();

    //     events = dao.getEvents();
    //     Assertions.assertTrue(!events.isEmpty());
    //     Assertions.assertTrue(events.size() == 3);

    //     unassignedEvents = dao.getUnassignedEvents();
    //     Assertions.assertTrue(!unassignedEvents.isEmpty());
    //     Assertions.assertTrue(unassignedEvents.size() == 2);
    // }

    // @Test
    // public void addEvent() throws Exception {
    //     List<Event> events = dao.getEvents();
    //     Assertions.assertTrue(events.isEmpty());
    //     Event event1 = new Event();
    //     event1.setName("Event 1");
    //     dao.tx = new EntityUserTransaction(em);
    //     dao.addEvent(event1);

    //     events = dao.getEvents();
    //     Assertions.assertTrue(!events.isEmpty());
    //     Assertions.assertTrue(events.size() == 1);
    // }


    // @Test
    // public void addExistingEvent() throws Exception {
    //     List<Event> events = dao.getEvents();
    //     Assertions.assertTrue(events.isEmpty());
    //     Event event1 = new Event();
    //     event1.setName("Event 1");
    //     dao.tx = new EntityUserTransaction(em);
    //     dao.addEvent(event1);

    //     Assertions.assertThrows(DaoException.class, () -> {
    //         dao.addEvent(event1);
    //     });
    // }

    // @Test 
    // public void testAddEventWithException() throws Exception {
    //     List<Event> events = dao.getEvents();
    //     Assertions.assertTrue(events.isEmpty());
    //     Event event1 = new Event();
    //     event1.setName("Event 1");
        
    //     EntityManager em = Mockito.mock(EntityManager.class);
    //     doThrow(NotSupportedException.class).when(em).find(any(), any());
        
    //     em.merge(event1);

    //     dao.em = em;

    //     Assertions.assertThrows(DaoException.class, () -> {
    //         dao.addEvent(event1);
    //     });
    // }

    // @Test
    // public void removeEvent() throws Exception {
    //     List<Event> events = dao.getEvents();
    //     Assertions.assertTrue(events.isEmpty());
    //     Event event1 = new Event();
    //     event1.setName("Event 1");
    //     dao.tx = new EntityUserTransaction(em);
    //     dao.addEvent(event1);

    //     events = dao.getEvents();
    //     Assertions.assertTrue(!events.isEmpty());
    //     Assertions.assertTrue(events.size() == 1);

    //     dao.deleteEvent(event1);
    //     events = dao.getEvents();
    //     Assertions.assertTrue(events.isEmpty());
    // }

    // @Test
    // public void removeEventNotExisting() throws Exception {
    //     List<Event> events = dao.getEvents();
    //     Assertions.assertTrue(events.isEmpty());
    //     Event event1 = new Event();
    //     event1.setName("Event 1");
    //     dao.tx = new EntityUserTransaction(em);
    //     dao.addEvent(event1);

    //     events = dao.getEvents();
    //     Assertions.assertTrue(!events.isEmpty());
    //     Assertions.assertTrue(events.size() == 1);

    //     Event event2 = new Event();
    //     event2.setName("Event 2");

    //     dao.deleteEvent(event2);
    //     events = dao.getEvents();
    //     Assertions.assertTrue(!events.isEmpty());
    //     Assertions.assertTrue(events.size() == 1);
    //     Assertions.assertEquals(events.get(0).getName(), "Event 1");
    // }

    // @Test
    // public void removeRaceEventWithException() throws Exception {
    //     List<Event> events = dao.getEvents();
    //     Assertions.assertTrue(events.isEmpty());
    //     Event event1 = new Event();
    //     event1.setName("Event 1");
    //     event1.setId(1L);

    //     EntityManager em = Mockito.mock(EntityManager.class);
    //     doThrow(NotSupportedException.class).when(em).find(any(), any());
        
    //     dao.tx = new EntityUserTransaction(em);
    //     dao.em = em;

    //     Assertions.assertThrows(DaoException.class, () -> {
    //         dao.deleteEvent(event1);
    //     });
    // }

    // @Test
    // public void updateRaceEvent() throws Exception {
    //     List<Event> events = dao.getEvents();
    //     Assertions.assertTrue(events.isEmpty());
    //     Event event1 = new Event();
    //     event1.setName("Event 1");
    //     dao.tx = new EntityUserTransaction(em);
    //     dao.addEvent(event1);

    //     events = dao.getEvents();
    //     Assertions.assertTrue(!events.isEmpty());
    //     Assertions.assertTrue(events.size() == 1);
    //     Assertions.assertEquals(events.get(0).getName(), "Event 1");

    //     event1.setName("new name");
    //     dao.updateEvent(event1);
    //     events = dao.getEvents();
    //     Assertions.assertTrue(!events.isEmpty());
    //     Assertions.assertTrue(events.size() == 1);
    //     Assertions.assertEquals(events.get(0).getName(), "new name");
    // }

    // @Test
    // public void updateRaceEventWithException() throws Exception {
    //     List<Event> raceEvents = dao.getEvents();
    //     Assertions.assertTrue(raceEvents.isEmpty());
    //     Event event1 = new Event();
    //     event1.setName("Event 1");

    //     UserTransaction userTransactionMock = Mockito.mock(UserTransaction.class);
    //     doThrow(HeuristicMixedException.class).when(userTransactionMock).commit();

    //     dao.tx = userTransactionMock;

    //     Assertions.assertThrows(DaoException.class, () -> {
    //         dao.updateEvent(event1);
    //     });
    // }
}
