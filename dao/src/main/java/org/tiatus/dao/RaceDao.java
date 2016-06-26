package org.tiatus.dao;

import org.tiatus.entity.Race;

import java.util.List;

/**
 * Created by johnreynolds on 19/06/2016.
 */
public interface RaceDao {
    List<Race> getRaces();
    Race addRace(Race race) throws DaoException, DaoEntityExistsException;
    void removeRace(Race race) throws DaoException;
    void updateRace(Race race) throws DaoException;

}
