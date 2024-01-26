package org.tiatus.service;

import org.tiatus.dao.DaoException;
import org.tiatus.dao.RaceDaoImpl;
import org.tiatus.entity.Race;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnreynolds on 14/09/2016.
 */
public class RaceServiceImplTest {

    // @Test
    // public void testConstructor() {
    //     RaceServiceImpl service = new RaceServiceImpl(new RaceDaoImpl(), new MessageSenderServiceImpl());
    // }

    // @Test
    // public void testAddRace() throws Exception {
    //     new MockUp<RaceDaoImpl>() {
    //         @Mock
    //         Race addRace(Race race) throws DaoException {
    //             return race;
    //         }
    //     };
    //     new MockUp<MessageSenderServiceImpl>() {
    //         @Mock
    //         public void sendMessage(final Message obj) throws JMSException {}
    //     };
    //     RaceServiceImpl service = new RaceServiceImpl(new RaceDaoImpl(), new MessageSenderServiceImpl());
    //     Race race = new Race();
    //     service.addRace(race, "id");
    // }

    // @Test (expected = ServiceException.class)
    // public void testAddRaceExcepton() throws Exception {
    //     new MockUp<RaceDaoImpl>() {
    //         @Mock
    //         Race addRace(Race race) throws DaoException {
    //             throw new DaoException("message");
    //         }
    //     };
    //     RaceServiceImpl service = new RaceServiceImpl(new RaceDaoImpl(), new MessageSenderServiceImpl());
    //     Race race = new Race();
    //     service.addRace(race, "id");
    // }

    // @Test
    // public void testDeleteRace() throws Exception {
    //     new MockUp<RaceDaoImpl>() {
    //         @Mock
    //         public void removeRace(Race race) throws DaoException {}
    //     };
    //     RaceServiceImpl service = new RaceServiceImpl(new RaceDaoImpl(), new MessageSenderServiceImpl());
    //     Race race = new Race();
    //     service.deleteRace(race, "id");
    // }

    // @Test (expected = ServiceException.class)
    // public void testDeleteRaceException() throws Exception {
    //     new MockUp<RaceDaoImpl>() {
    //         @Mock
    //         public void removeRace(Race race) throws DaoException {
    //             throw new DaoException("message");
    //         }
    //     };
    //     RaceServiceImpl service = new RaceServiceImpl(new RaceDaoImpl(), new MessageSenderServiceImpl());
    //     Race race = new Race();
    //     service.deleteRace(race, "id");
    // }

    // @Test
    // public void testGetRaces() throws Exception {
    //     new MockUp<RaceDaoImpl>() {
    //         @Mock
    //         public List<Race> getRaces() {
    //             List<Race> races = new ArrayList<>();
    //             Race race = new Race();
    //             race.setId(1L);
    //             race.setRaceOrder(1);
    //             race.setName("Race 1");
    //             races.add(race);
    //             return races;
    //         }
    //     };
    //     RaceServiceImpl service = new RaceServiceImpl(new RaceDaoImpl(), new MessageSenderServiceImpl());
    //     service.getRaces();
    // }
}
