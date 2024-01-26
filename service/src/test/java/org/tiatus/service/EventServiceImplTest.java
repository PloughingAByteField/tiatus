package org.tiatus.service;

import org.tiatus.dao.DaoException;
import org.tiatus.dao.EventDaoImpl;
import org.tiatus.dao.RaceEventDao;
import org.tiatus.dao.RaceEventDaoImpl;
import org.tiatus.entity.Event;
import org.tiatus.entity.Race;
import org.tiatus.entity.RaceEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnreynolds on 14/09/2016.
 */
public class EventServiceImplTest {

    // @Test
    // public void testConstructor() {
    //     EventServiceImpl service = new EventServiceImpl(new EventDaoImpl(), new RaceEventDaoImpl(), new MessageSenderServiceImpl());
    // }

    // @Test
    // public void testAddEvent() throws Exception {
    //     new MockUp<EventDaoImpl>() {
    //         @Mock
    //         Event addEvent(Event event) throws DaoException {
    //             return event;
    //         }
    //     };
    //     new MockUp<MessageSenderServiceImpl>() {
    //         @Mock
    //         public void sendMessage(final Message obj) throws JMSException {}
    //     };
    //     EventServiceImpl service = new EventServiceImpl(new EventDaoImpl(), new RaceEventDaoImpl(), new MessageSenderServiceImpl());
    //     Event event = new Event();
    //     service.addEvent(event, "id");
    // }

    // @Test (expected = ServiceException.class)
    // public void testAddRaceException() throws Exception {
    //     new MockUp<EventDaoImpl>() {
    //         @Mock
    //         Event addEvent(Event event) throws DaoException {
    //             throw new DaoException("message");
    //         }
    //     };
    //     EventServiceImpl service = new EventServiceImpl(new EventDaoImpl(), new RaceEventDaoImpl(), new MessageSenderServiceImpl());
    //     Event event = new Event();
    //     service.addEvent(event, "id");
    // }

    // @Test
    // public void testAddRaceEvent() throws Exception {
    //     new MockUp<RaceEventDaoImpl>() {
    //         @Mock
    //         RaceEvent addRaceEvent(RaceEvent event) throws DaoException {
    //             return event;
    //         }
    //     };
    //     EventServiceImpl service = new EventServiceImpl(new EventDaoImpl(), new RaceEventDaoImpl(), new MessageSenderServiceImpl());
    //     RaceEvent event = new RaceEvent();
    //     service.addRaceEvent(event, "id");
    // }

    // @Test (expected = ServiceException.class)
    // public void testAddRaceEventException() throws Exception {
    //     new MockUp<RaceEventDaoImpl>() {
    //         @Mock
    //         RaceEvent addRaceEvent(RaceEvent event) throws DaoException {
    //             throw new DaoException("message");
    //         }
    //     };
    //     EventServiceImpl service = new EventServiceImpl(new EventDaoImpl(), new RaceEventDaoImpl(), new MessageSenderServiceImpl());
    //     RaceEvent event = new RaceEvent();
    //     service.addRaceEvent(event, "id");
    // }

    // @Test
    // public void testDeleteRace() throws Exception {
    //     new MockUp<EventDaoImpl>() {
    //         @Mock
    //         public void deleteEvent(Event event) throws DaoException {}
    //     };
    //     EventServiceImpl service = new EventServiceImpl(new EventDaoImpl(), new RaceEventDaoImpl(), new MessageSenderServiceImpl());
    //     Event event = new Event();
    //     service.deleteEvent(event, "id");
    // }

    // @Test (expected = ServiceException.class)
    // public void testDeleteRaceException() throws Exception {
    //     new MockUp<EventDaoImpl>() {
    //         @Mock
    //         public void deleteEvent(Event event) throws DaoException {
    //             throw new DaoException("message");
    //         }
    //     };
    //     EventServiceImpl service = new EventServiceImpl(new EventDaoImpl(), new RaceEventDaoImpl(), new MessageSenderServiceImpl());
    //     Event event = new Event();
    //     service.deleteEvent(event, "id");
    // }

    // @Test
    // public void testDeleteRaceEvent() throws Exception {
    //     new MockUp<RaceEventDaoImpl>() {
    //         @Mock
    //         public void deleteRaceEvent(RaceEvent event) throws DaoException {}
    //     };
    //     EventServiceImpl service = new EventServiceImpl(new EventDaoImpl(), new RaceEventDaoImpl(), new MessageSenderServiceImpl());
    //     RaceEvent event = new RaceEvent();
    //     service.deleteRaceEvent(event, "id");
    // }

    // @Test (expected = ServiceException.class)
    // public void testDeleteRaceEventException() throws Exception {
    //     new MockUp<RaceEventDaoImpl>() {
    //         @Mock
    //         public void deleteRaceEvent(RaceEvent event) throws DaoException {
    //             throw new DaoException("message");
    //         }
    //     };
    //     EventServiceImpl service = new EventServiceImpl(new EventDaoImpl(), new RaceEventDaoImpl(), new MessageSenderServiceImpl());
    //     RaceEvent event = new RaceEvent();
    //     service.deleteRaceEvent(event, "id");
    // }

    // @Test
    // public void testGetEvents() throws Exception {
    //     new MockUp<EventDaoImpl>() {
    //         @Mock
    //         public List<Event> getEvents() {
    //             List<Event> events = new ArrayList<>();
    //             Event event = new Event();
    //             event.setId(1L);
    //             event.setName("Event 1");
    //             events.add(event);
    //             return events;
    //         }
    //     };
    //     EventServiceImpl service = new EventServiceImpl(new EventDaoImpl(), new RaceEventDaoImpl(), new MessageSenderServiceImpl());
    //     List<Event> events = service.getEvents();
    //     Assert.assertFalse(events.isEmpty());
    //     Assert.assertEquals(events.get(0).getName(), "Event 1");
    // }

    // @Test
    // public void testGetUnassigedEvents() throws Exception {
    //     new MockUp<EventDaoImpl>() {
    //         @Mock
    //         public List<Event> getUnassignedEvents() {
    //             List<Event> events = new ArrayList<>();
    //             Event event = new Event();
    //             event.setId(1L);
    //             event.setName("Event 1");
    //             events.add(event);
    //             return events;
    //         }
    //     };
    //     EventServiceImpl service = new EventServiceImpl(new EventDaoImpl(), new RaceEventDaoImpl(), new MessageSenderServiceImpl());
    //     List<Event> events = service.getUnassignedEvents();
    //     Assert.assertFalse(events.isEmpty());
    //     Assert.assertEquals(events.get(0).getName(), "Event 1");
    // }

    // @Test
    // public void testGetAssignedEvents() throws Exception {
    //     new MockUp<RaceEventDaoImpl>() {
    //         @Mock
    //         public List<RaceEvent> getRaceEvents() {
    //             List<RaceEvent> raceEvents = new ArrayList<>();
    //             RaceEvent raceEvent = new RaceEvent();
    //             Race race = new Race();
    //             race.setId(1L);
    //             Event event = new Event();
    //             event.setId(1L);
    //             event.setName("Event 1");
    //             raceEvent.setId(1L);
    //             raceEvent.setEvent(event);
    //             raceEvent.setRace(race);
    //             raceEvents.add(raceEvent);
    //             return raceEvents;
    //         }
    //     };
    //     EventServiceImpl service = new EventServiceImpl(new EventDaoImpl(), new RaceEventDaoImpl(), new MessageSenderServiceImpl());
    //     List<RaceEvent> events = service.getAssignedEvents();
    //     Assert.assertFalse(events.isEmpty());
    //     Assert.assertEquals(events.get(0).getEvent().getName(), "Event 1");
    // }

    // private List<RaceEvent> getRaceEvents() {
    //     List<RaceEvent> raceEvents = new ArrayList<>();
    //     RaceEvent raceEvent = new RaceEvent();
    //     Race race = new Race();
    //     race.setId(1L);
    //     Event event = new Event();
    //     event.setId(1L);
    //     event.setName("Event 1");
    //     raceEvent.setId(1L);
    //     raceEvent.setEvent(event);
    //     raceEvent.setRace(race);
    //     raceEvents.add(raceEvent);
    //     return raceEvents;
    // }

    // @Test
    // public void testUpdateRaceEvents() throws Exception {
    //     new MockUp<RaceEventDaoImpl>() {
    //         @Mock
    //         public void updateRaceEvent(RaceEvent raceEvent) throws DaoException {

    //         }

    //         @Mock
    //         public RaceEvent getRaceEventByEvent(Event event) throws DaoException {
    //             return getRaceEvents().get(0);
    //         }
    //     };
    //     EventServiceImpl service = new EventServiceImpl(new EventDaoImpl(), new RaceEventDaoImpl(), new MessageSenderServiceImpl());

    //     service.updateRaceEvents(getRaceEvents(), "id");
    // }

    // @Test (expected = ServiceException.class)
    // public void testUpdateRaceEventsDaoException() throws Exception {
    //     new MockUp<RaceEventDaoImpl>() {
    //         @Mock
    //         public void updateRaceEvent(RaceEvent raceEvent) throws DaoException {
    //             throw new DaoException("error");
    //         }

    //         @Mock
    //         public RaceEvent getRaceEventByEvent(Event event) throws DaoException {
    //             return getRaceEvents().get(0);
    //         }
    //     };
    //     EventServiceImpl service = new EventServiceImpl(new EventDaoImpl(), new RaceEventDaoImpl(), new MessageSenderServiceImpl());

    //     service.updateRaceEvents(getRaceEvents(), "id");
    // }
}
