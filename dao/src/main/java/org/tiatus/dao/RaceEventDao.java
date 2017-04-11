package org.tiatus.dao;

import org.tiatus.entity.Event;
import org.tiatus.entity.Race;
import org.tiatus.entity.RaceEvent;

import java.util.List;

/**
 * Created by johnreynolds on 10/10/2016.
 */
public interface RaceEventDao {
    /**
     * Add a RaceEvent
     *
     * @param raceEvent RaceEvent to create
     * @return created RaceEvent
     * @throws DaoException on error
     */
    RaceEvent addRaceEvent(RaceEvent raceEvent) throws DaoException;

    /**
     * Remove a RaceEvent
     *
     * @param raceEvent RaceEvent to remove
     * @throws DaoException on error
     */
    void deleteRaceEvent(RaceEvent raceEvent) throws DaoException;

    /**
     * Update a RaceEvent
     * @param raceEvent RaceEvent to update
     * @throws DaoException on error
     */
    void updateRaceEvent(RaceEvent raceEvent) throws DaoException;

    /**
     * Get list of RaceEvents
     *
     * @return list of race events
     */
    List<RaceEvent> getRaceEvents();

    /**
     * Get list of RaceEvents for a race
     *
     * @param race Race
     * @return list of race events
     */
    List<RaceEvent> getRaceEventsForRace(Race race);

    /**
     * Get RaceEvent
     *
     * @param event Event
     * @return RaceEvent containing event or null
     */
    RaceEvent getRaceEventByEvent(Event event);

    RaceEvent getRaceEventForId(Long id);
}