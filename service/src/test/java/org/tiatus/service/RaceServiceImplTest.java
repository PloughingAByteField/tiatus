package org.tiatus.service;

import mockit.Mock;
import mockit.MockUp;
import org.junit.Test;
import org.tiatus.dao.DaoException;
import org.tiatus.dao.RaceDaoImpl;
import org.tiatus.entity.Race;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnreynolds on 14/09/2016.
 */
public class RaceServiceImplTest {

    @Test
    public void testConstructor() {
        RaceServiceImpl service = new RaceServiceImpl(new RaceDaoImpl());
    }

    @Test
    public void testAddRace() throws Exception {
        new MockUp<RaceDaoImpl>() {
            @Mock
            Race addRace(Race race) throws DaoException {
                return race;
            }
        };
        RaceServiceImpl service = new RaceServiceImpl(new RaceDaoImpl());
        Race race = new Race();
        service.addRace(race);
    }

    @Test (expected = ServiceException.class)
    public void testAddRaceExcepton() throws Exception {
        new MockUp<RaceDaoImpl>() {
            @Mock
            Race addRace(Race race) throws DaoException {
                throw new DaoException("message");
            }
        };
        RaceServiceImpl service = new RaceServiceImpl(new RaceDaoImpl());
        Race race = new Race();
        service.addRace(race);
    }

    @Test
    public void testDeleteRace() throws Exception {
        new MockUp<RaceDaoImpl>() {
            @Mock
            public void removeRace(Race race) throws DaoException {}
        };
        RaceServiceImpl service = new RaceServiceImpl(new RaceDaoImpl());
        Race race = new Race();
        service.deleteRace(race);
    }

    @Test (expected = ServiceException.class)
    public void testDeleteRaceException() throws Exception {
        new MockUp<RaceDaoImpl>() {
            @Mock
            public void removeRace(Race race) throws DaoException {
                throw new DaoException("message");
            }
        };
        RaceServiceImpl service = new RaceServiceImpl(new RaceDaoImpl());
        Race race = new Race();
        service.deleteRace(race);
    }

    @Test
    public void testGetRaces() throws Exception {
        new MockUp<RaceDaoImpl>() {
            @Mock
            public List<Race> getRaces() {
                List<Race> races = new ArrayList<>();
                Race race = new Race();
                race.setId(1L);
                race.setRaceOrder(1);
                race.setName("Race 1");
                races.add(race);
                return races;
            }
        };
        RaceServiceImpl service = new RaceServiceImpl(new RaceDaoImpl());
        service.getRaces();
    }
}
