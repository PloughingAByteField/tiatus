package org.tiatus.dao;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.tiatus.entity.Event;
import org.tiatus.entity.Race;
import org.tiatus.entity.RaceEvent;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnreynolds on 14/09/2016.
 */
@ExtendWith(MockitoExtension.class)
public class RaceEventDaoTest {

    @Mock
    private EntityManager entityManagerMock;

    @Mock
    private UserTransaction userTransactionMock;

    @Mock
    private TypedQuery typedQueryMock;

    @Test
    public void testAddRace() throws DaoException {
        when(typedQueryMock.getResultList()).thenReturn(new ArrayList<>());
        when(typedQueryMock.setParameter(anyString(), any())).thenReturn(typedQueryMock);
        when(entityManagerMock.find(any(), any())).thenReturn(null);
        when(entityManagerMock.createQuery(any(), any())).thenReturn(typedQueryMock);

        RaceEventDaoImpl dao = new RaceEventDaoImpl();
        dao.em = entityManagerMock;
        dao.tx = userTransactionMock;
        RaceEvent raceEvent = new RaceEvent();
        Race race = new Race();
        race.setId(1L);
        Event event = new Event();
        event.setId(1L);
        raceEvent.setId(1L);
        raceEvent.setEvent(event);
        raceEvent.setRace(race);
        dao.addRaceEvent(raceEvent);
    }

    @Test 
    public void testAddRaceEventExisting() throws DaoException {
        RaceEvent existingRaceEvent = new RaceEvent();
        existingRaceEvent.setId(1L);
        when(entityManagerMock.find(any(), any())).thenReturn(existingRaceEvent);


        RaceEventDaoImpl dao = new RaceEventDaoImpl();
        dao.em = entityManagerMock;
        dao.tx = userTransactionMock;
        RaceEvent raceEvent = new RaceEvent();
        Race race = new Race();
        race.setId(1L);
        Event event = new Event();
        event.setId(1L);
        raceEvent.setId(1L);
        raceEvent.setEvent(event);
        raceEvent.setRace(race);

        Assertions.assertThrows(DaoException.class, () -> {
            dao.addRaceEvent(raceEvent);
        });
    }

    @Test
    public void testAddRaceEventException() throws Exception {
        when(typedQueryMock.getResultList()).thenReturn(new ArrayList<>());
        when(typedQueryMock.setParameter(anyString(), any())).thenReturn(typedQueryMock);
        when(entityManagerMock.find(any(), any())).thenReturn(null);
        when(entityManagerMock.createQuery(any(), any())).thenReturn(typedQueryMock);

        doThrow(HeuristicMixedException.class).when(userTransactionMock).commit();

        RaceEventDaoImpl dao = new RaceEventDaoImpl();
        dao.em = entityManagerMock;
        dao.tx = userTransactionMock;
        RaceEvent raceEvent = new RaceEvent();
        Race race = new Race();
        race.setId(1L);
        Event event = new Event();
        event.setId(1L);
        raceEvent.setId(1L);
        raceEvent.setEvent(event);
        raceEvent.setRace(race);

        Assertions.assertThrows(DaoException.class, () -> {
            dao.addRaceEvent(raceEvent);
        });
    }

    private List<RaceEvent> getRaceEvents() {
        List<RaceEvent> list = new ArrayList<>();
        RaceEvent raceEvent = new RaceEvent();
        Race race = new Race();
        race.setId(1L);
        Event event = new Event();
        event.setId(1L);
        raceEvent.setId(1L);
        raceEvent.setEvent(event);
        raceEvent.setRace(race);
        list.add(raceEvent);
        return list;
    }

    @Test
    public void testGetRaceEvents() {
        when(entityManagerMock.createQuery(any(), any())).thenReturn(typedQueryMock);
        when(typedQueryMock.getResultList()).thenReturn(getRaceEvents());
        RaceEventDaoImpl dao = new RaceEventDaoImpl();
        dao.em = entityManagerMock;

        Assertions.assertFalse(dao.getRaceEvents().isEmpty());
    }

    @Test
    public void testGetRaceEventByEvent() {
        when(entityManagerMock.createQuery(any(), any())).thenReturn(typedQueryMock);
        when(typedQueryMock.setParameter(anyString(), any())).thenReturn(typedQueryMock);

        RaceEventDaoImpl dao = new RaceEventDaoImpl();
        dao.em = entityManagerMock;
        dao.getRaceEventByEvent(getRaceEvents().get(0).getEvent());
    }

    @Test
    public void testRemoveRaceEvent() throws Exception {
        RaceEvent existingRaceEvent = new RaceEvent();
        Race race = new Race();
        race.setId(1L);
        Event event = new Event();
        event.setId(1L);
        existingRaceEvent.setId(1L);
        existingRaceEvent.setEvent(event);
        existingRaceEvent.setRace(race);

        when(entityManagerMock.find(Mockito.eq(RaceEvent.class), any())).thenReturn(existingRaceEvent);

        RaceEventDaoImpl dao = new RaceEventDaoImpl();
        dao.em = entityManagerMock;
        dao.tx = userTransactionMock;
        RaceEvent raceEvent = new RaceEvent();
        raceEvent.setId(1L);
        raceEvent.setEvent(event);
        raceEvent.setRace(race);
        dao.deleteRaceEvent(raceEvent);
    }

    @Test
    public void testRemoveRaceEventNoRaceEvent() throws Exception {
        when(entityManagerMock.find(any(), any())).thenReturn(null);

        RaceEventDaoImpl dao = new RaceEventDaoImpl();
        dao.em = entityManagerMock;
        dao.tx = userTransactionMock;
        RaceEvent raceEvent = new RaceEvent();
        Race race = new Race();
        race.setId(1L);
        Event event = new Event();
        event.setId(1L);
        raceEvent.setId(1L);
        raceEvent.setEvent(event);
        raceEvent.setRace(race);
        dao.deleteRaceEvent(raceEvent);
    }

    @Test
    public void testRemoveRaceEventException() throws Exception {
        RaceEvent existingRaceEvent = new RaceEvent();
        existingRaceEvent.setId(1L);
        when(entityManagerMock.find(any(), any())).thenReturn(existingRaceEvent);

        doThrow(HeuristicMixedException.class).when(userTransactionMock).commit();

        RaceEventDaoImpl dao = new RaceEventDaoImpl();
        dao.em = entityManagerMock;
        dao.tx = userTransactionMock;
        RaceEvent raceEvent = new RaceEvent();
        Race race = new Race();
        race.setId(1L);
        Event event = new Event();
        event.setId(1L);
        raceEvent.setId(1L);
        raceEvent.setEvent(event);
        raceEvent.setRace(race);

        Assertions.assertThrows(DaoException.class, () -> {
            dao.deleteRaceEvent(raceEvent);
        });
    }

    @Test
    public void testUpdateRaceEvent() throws Exception {
        RaceEventDaoImpl dao = new RaceEventDaoImpl();
        dao.em = entityManagerMock;
        dao.tx = userTransactionMock;
        RaceEvent raceEvent = new RaceEvent();
        Race race = new Race();
        race.setId(1L);
        Event event = new Event();
        event.setId(1L);
        raceEvent.setId(1L);
        raceEvent.setEvent(event);
        raceEvent.setRace(race);
        dao.updateRaceEvent(raceEvent);
    }

    @Test
    public void testUpdateRaceEventException() throws Exception {
        doThrow(HeuristicMixedException.class).when(userTransactionMock).commit();

        RaceEventDaoImpl dao = new RaceEventDaoImpl();
        dao.em = entityManagerMock;
        dao.tx = userTransactionMock;
        RaceEvent raceEvent = new RaceEvent();
        Race race = new Race();
        race.setId(1L);
        Event event = new Event();
        event.setId(1L);
        raceEvent.setId(1L);
        raceEvent.setEvent(event);
        raceEvent.setRace(race);

        Assertions.assertThrows(DaoException.class, () -> {
            dao.updateRaceEvent(raceEvent);
        });
    }
}
