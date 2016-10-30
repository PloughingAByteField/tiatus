package org.tiatus.service;

import org.tiatus.entity.Event;
import org.tiatus.entity.RaceEvent;

import java.util.List;

/**
 * Created by johnreynolds on 25/06/2016.
 */
public interface EventService {
    /**
     * Add Event
     * @param event Event to create
     * @return created Event
     * @throws ServiceException on error
     */
    Event addEvent(Event event) throws ServiceException;

    /**
     * Add RaceEvent
     * @param raceEvent RaceEvent to create
     * @return created RaceEvent
     * @throws ServiceException on error
     */
    RaceEvent addRaceEvent(RaceEvent raceEvent) throws ServiceException;

    /**
     * Remove a Event
     * @param event Event to remove
     * @throws ServiceException on error
     */
    void deleteEvent(Event event) throws ServiceException;

    /**
     * Remove a RaceEvent
     * @param raceEvent RaceEvent to remove
     * @throws ServiceException on error
     */
    void deleteRaceEvent(RaceEvent raceEvent) throws ServiceException;

    /**
     * Get events
     * @return list of events
     */
    List<Event> getEvents();

    /**
     * Get assigned events
     * @return list of events assigned to a race
     */
    List<RaceEvent> getAssignedEvents();

    /**
     * Get unassigned events
     * @return list of events assigned not yet to a race
     */
    List<Event> getUnassignedEvents();
}
