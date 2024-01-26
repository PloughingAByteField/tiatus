package org.tiatus.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.tiatus.entity.Race;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnreynolds on 14/09/2016.
 */
@ExtendWith(MockitoExtension.class)
public class RaceDaoTest {

    // @Mock
    // private EntityManager entityManagerMock;

    // @Mock
    // private UserTransaction userTransactionMock;

    // @Mock
    // private TypedQuery typedQueryMock;

    // @Test
    // public void testAddRace() throws DaoException {
    //     when(entityManagerMock.find(any(), any())).thenReturn(null);

    //     RaceDaoImpl dao = new RaceDaoImpl();
    //     dao.em = entityManagerMock;
    //     dao.tx = userTransactionMock;
    //     Race race = new Race();
    //     race.setId(1L);
    //     dao.addRace(race);
    // }

    // @Test 
    // public void testAddRaceExisting() throws DaoException {
    //     Race existingRace = new Race();
    //     existingRace.setId(1L);
    //     when(entityManagerMock.find(any(), any())).thenReturn(existingRace);

    //     RaceDaoImpl dao = new RaceDaoImpl();
    //     dao.em = entityManagerMock;
    //     dao.tx = userTransactionMock;
    //     Race race = new Race();
    //     race.setId(1L);

    //     Assertions.assertThrows(DaoException.class, () -> {
    //         dao.addRace(race);
    //     });
    // }

    // @Test
    // public void testAddRaceException() throws Exception {
    //     when(entityManagerMock.find(any(), any())).thenReturn(null);

    //     doThrow(HeuristicMixedException.class).when(userTransactionMock).commit();

    //     RaceDaoImpl dao = new RaceDaoImpl();
    //     dao.em = entityManagerMock;
    //     dao.tx = userTransactionMock;
    //     Race race = new Race();
    //     race.setId(1L);

    //     Assertions.assertThrows(DaoException.class, () -> {
    //         dao.addRace(race);
    //     });
    // }

    // @Test
    // public void testGetRaces() {
    //     List<Race> list = new ArrayList<>();
    //     Race existingRace = new Race();
    //     list.add(existingRace);
        
    //     when(typedQueryMock.getResultList()).thenReturn(list);
    //     when(entityManagerMock.createQuery(any(), any())).thenReturn(typedQueryMock);

    //     RaceDaoImpl dao = new RaceDaoImpl();
    //     dao.em = entityManagerMock;
    //     Assertions.assertFalse(dao.getRaces().isEmpty());
    // }

    // @Test
    // public void testRemoveRace() throws Exception {
    //     Race existingRace = new Race();
    //     existingRace.setId(1L);

    //     when(entityManagerMock.find(any(), any())).thenReturn(existingRace);

    //     RaceDaoImpl dao = new RaceDaoImpl();
    //     dao.em = entityManagerMock;
    //     dao.tx = userTransactionMock;
    //     Race race = new Race();
    //     race.setId(1L);
    //     dao.removeRace(race);
    // }

    // @Test
    // public void testRemoveRaceNoRace() throws Exception {
        
    //     when(entityManagerMock.find(any(), any())).thenReturn(null);
        
    //     RaceDaoImpl dao = new RaceDaoImpl();
    //     dao.em = entityManagerMock;
    //     dao.tx = userTransactionMock;
    //     Race race = new Race();
    //     race.setId(1L);
    //     dao.removeRace(race);
    // }

    // @Test 
    // public void testRemoveRaceException() throws Exception {
    //     Race existingRace = new Race();
    //     existingRace.setId(1L);
        
    //     when(entityManagerMock.find(any(), any())).thenReturn(existingRace);

    //     doThrow(HeuristicMixedException.class).when(userTransactionMock).commit();

    //     RaceDaoImpl dao = new RaceDaoImpl();
    //     dao.em = entityManagerMock;
    //     dao.tx = userTransactionMock;
    //     Race race = new Race();
    //     race.setId(1L);

    //     Assertions.assertThrows(DaoException.class, () -> {
    //         dao.removeRace(race);
    //     });
    // }

    // @Test
    // public void testUpdateRace() throws Exception {
    //     RaceDaoImpl dao = new RaceDaoImpl();
    //     dao.em = entityManagerMock;
    //     dao.tx = userTransactionMock;
    //     Race race = new Race();
    //     race.setId(1L);
    //     dao.updateRace(race);
    // }

    // @Test
    // public void testUpdateRaceException() throws Exception {
    //     doThrow(HeuristicMixedException.class).when(userTransactionMock).commit();

    //     RaceDaoImpl dao = new RaceDaoImpl();
    //     dao.em = entityManagerMock;
    //     dao.tx = userTransactionMock;
    //     Race race = new Race();
    //     race.setId(1L);

    //     Assertions.assertThrows(DaoException.class, () -> {
    //         dao.updateRace(race);
    //     });
    // }
}
