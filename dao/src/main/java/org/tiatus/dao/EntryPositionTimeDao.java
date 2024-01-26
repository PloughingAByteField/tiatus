package org.tiatus.dao;

import org.tiatus.entity.EntryPositionTime;
import org.tiatus.entity.Position;
import org.tiatus.entity.Race;

import java.util.List;

/**
 * Created by johnreynolds on 19/06/2016.
 */
public interface EntryPositionTimeDao {
    /**
     * Get times for entries at position for race
     * @param race Race
     * @param position Position
     * @return a list of Races
     */
    List<EntryPositionTime> getPositionTimesForPositionInRace(Race race, Position position) throws DaoException;

    /**
     * Get times for entries for race
     * @param race Race
     * @return a list of Races
     */
    List<EntryPositionTime> getTimesForRace(Race race) throws DaoException;

    List<EntryPositionTime> getAllTimesForRace(Race race) throws DaoException;

    /**
     * Record a time for entry at position in a race
     * @throws DaoException on error
     */
    void createTime(EntryPositionTime entryPositionTime) throws DaoException;

    void updateTime(EntryPositionTime entryPositionTime) throws DaoException;


}
