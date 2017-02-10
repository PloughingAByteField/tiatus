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
     * @param race Race to add
     * @param position Position to add
     * @return a list of Races
     */
    List<EntryPositionTime> getPositionTimesForPositionInRace(Race race, Position position) throws DaoException;


    /**
     * Record a time for entry at position in a race
     * @return EntryPositionTime Added entry position time
     * @throws DaoException on error
     */
    EntryPositionTime createTimeForEntry(EntryPositionTime time) throws DaoException;

}
