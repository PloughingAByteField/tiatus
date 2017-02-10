package org.tiatus.dao;

import org.tiatus.entity.Race;

import java.util.List;

/**
 * Created by johnreynolds on 19/06/2016.
 */
public interface RaceDao {
    /**
     * Get Race for a given id
     * @return Race or null
     */
    Race getRaceForId(Long id);

    /**
     * Get Races
     * @return a list of Races
     */
    List<Race> getRaces();

    /**
     * Add a new Race
     * @param race Race to add
     * @return Race Added race
     * @throws DaoException on error
     */
    Race addRace(Race race) throws DaoException;

    /**
     * Remove a Race
     * @param race Race to remove
     * @throws DaoException on error
     */
    void removeRace(Race race) throws DaoException;

    /**
     * Update a Race
     * @param race Race to update
     * @throws DaoException on error
     */
    void updateRace(Race race) throws DaoException;

}
