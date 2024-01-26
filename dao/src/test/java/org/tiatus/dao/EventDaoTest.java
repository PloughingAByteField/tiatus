package org.tiatus.dao;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import org.tiatus.entity.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnreynolds on 14/09/2016.
 */
@ExtendWith(MockitoExtension.class)
public class EventDaoTest {

    // @Mock
    // private EntityManager entityManagerMock;

    // @Mock
    // private UserTransaction userTransactionMock;

    // @Mock
    // private TypedQuery typedQueryMock;

    // @Test
    // public void testAddEvent() throws DaoException {
    //     when(typedQueryMock.getResultList()).thenReturn(new ArrayList<>());
    //     when(entityManagerMock.createQuery(any(), any())).thenReturn(typedQueryMock);

    //     EventDaoImpl dao = new EventDaoImpl();
    //     dao.em = entityManagerMock;
    //     dao.tx = userTransactionMock;
    //     Event event = new Event();
    //     event.setId(1L);
    //     dao.addEvent(event);
    // }

    // @Test
    // public void testAddEventExisting() throws DaoException {
    //     Event existingEvent = new Event();
    //     existingEvent.setId(1L);

    //     when(entityManagerMock.find(any(), any())).thenReturn(existingEvent);

    //     EventDaoImpl dao = new EventDaoImpl();
    //     dao.em = entityManagerMock;
    //     dao.tx = userTransactionMock;
    //     Event event = new Event();
    //     event.setId(1L);

    //     Assertions.assertThrows(DaoException.class, () -> {
    //         dao.addEvent(event);
    //     });
    // }

    // @Test
    // public void testAddEventException() throws Exception {
    //     when(entityManagerMock.find(any(), any())).thenReturn(null);
        
    //     EventDaoImpl dao = new EventDaoImpl();
    //     dao.em = entityManagerMock;
    //     dao.tx = userTransactionMock;
    //     Event event = new Event();
    //     event.setId(1L);

    //     Assertions.assertThrows(DaoException.class, () -> {
    //         dao.addEvent(event);
    //     });
    // }

    // @Test
    // public void testGetEvents() {
    //     List<Event> list = new ArrayList<>();
    //     Event existingEvents = new Event();
    //     list.add(existingEvents);

    //     when(typedQueryMock.getResultList()).thenReturn(list);
    //     when(entityManagerMock.createQuery(any(), any())).thenReturn(typedQueryMock);

    //     EventDaoImpl dao = new EventDaoImpl();
    //     dao.em = entityManagerMock;
    //     Assertions.assertFalse(dao.getEvents().isEmpty());
    // }

    // @Test
    // public void testGetUnasignedEvents() {
    //     List<Event> list = new ArrayList<>();
    //     Event existingEvents = new Event();
    //     list.add(existingEvents);

    //     when(typedQueryMock.getResultList()).thenReturn(list);
    //     when(entityManagerMock.createQuery(any(), any())).thenReturn(typedQueryMock);

    //     EventDaoImpl dao = new EventDaoImpl();
    //     dao.em = entityManagerMock;
    //     Assertions.assertFalse(dao.getUnassignedEvents().isEmpty());
    // }

    // @Test
    // public void testRemoveEvent() throws Exception {
    //     Event existingEvent = new Event();
    //     existingEvent.setId(1L);

    //     when(entityManagerMock.find(any(), any())).thenReturn(existingEvent);

    //     EventDaoImpl dao = new EventDaoImpl();
    //     dao.em = entityManagerMock;
    //     dao.tx = userTransactionMock;
    //     Event event = new Event();
    //     event.setId(1L);
    //     dao.deleteEvent(event);
    // }

    // @Test
    // public void testRemoveEventNoEvent() throws Exception {
    //     when(entityManagerMock.find(any(), any())).thenReturn(null);

    //     EventDaoImpl dao = new EventDaoImpl();
    //     dao.em = entityManagerMock;
    //     dao.tx = userTransactionMock;
    //     Event event = new Event();
    //     event.setId(1L);
    //     dao.deleteEvent(event);
    // }

    // @Test 
    // public void testRemoveEventException() throws Exception {
    //     doThrow(HeuristicMixedException.class).when(userTransactionMock).commit();

    //     Event existingEvent = new Event();
    //     existingEvent.setId(1L);

    //     when(entityManagerMock.find(any(), any())).thenReturn(existingEvent);

    //     EventDaoImpl dao = new EventDaoImpl();
    //     dao.em = entityManagerMock;
    //     dao.tx = userTransactionMock;
    //     Event event = new Event();
    //     event.setId(1L);
        
    //     Assertions.assertThrows(DaoException.class, () -> {
    //         dao.deleteEvent(event);
    //     });
    // }

    // @Test
    // public void testUpdateEvent() throws Exception {
    //     Event existingEvent = new Event();
    //     existingEvent.setId(1L);
    //     existingEvent.setName("name");
        
    //     when(entityManagerMock.find(any(), any())).thenReturn(existingEvent);

    //     EventDaoImpl dao = new EventDaoImpl();
    //     dao.em = entityManagerMock;
    //     dao.tx = userTransactionMock;
    //     Event event = new Event();
    //     event.setId(1L);
    //     event.setName("name");
    //     dao.updateEvent(event);
    // }

    // @Test 
    // public void testUpdateEventException() throws Exception {
    //     Event existingEvent = new Event();
    //     existingEvent.setId(1L);
    //     existingEvent.setName("name");
        
    //     when(entityManagerMock.find(any(), any())).thenReturn(existingEvent);

    //     doThrow(HeuristicMixedException.class).when(userTransactionMock).commit();

    //     EventDaoImpl dao = new EventDaoImpl();
    //     dao.em = entityManagerMock;
    //     dao.tx = userTransactionMock;
    //     Event event = new Event();
    //     event.setId(1L);

    //     Assertions.assertThrows(DaoException.class, () -> {
    //         dao.updateEvent(event);
    //     });
    // }
}
