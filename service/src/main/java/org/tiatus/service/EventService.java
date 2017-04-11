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
    Event addEvent(Event event, String sessionId) throws ServiceException;

    /**
     * Add RaceEvent
     * @param raceEvent RaceEvent to create
     * @return created RaceEvent
     * @throws ServiceException on error
     */
    RaceEvent addRaceEvent(RaceEvent raceEvent, String sessionId) throws ServiceException;

    /**
     * Update Event
     * @param event Event to update
     * @return updated Event
     * @throws ServiceException on error
     */
    Event updateEvent(Event event, String sessionId) throws ServiceException;

    /**
     * Remove a Event
     * @param event Event to remove
     * @throws ServiceException on error
     */
    void deleteEvent(Event event, String sessionId) throws ServiceException;

    /**
     * Remove a RaceEvent
     * @param raceEvent RaceEvent to remove
     * @throws ServiceException on error
     */
    void deleteRaceEvent(RaceEvent raceEvent, String sessionId) throws ServiceException;

    /**
     * Update a RaceEvents
     * @param raceEvents List of RaceEvents to update
     * @throws ServiceException on error
     */
    void updateRaceEvents(List<RaceEvent> raceEvents, String sessionId) throws ServiceException;

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

    Event getEventForId(Long id);
    RaceEvent getRaceEventForId(Long id);
}
