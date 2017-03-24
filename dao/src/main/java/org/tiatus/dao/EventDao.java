package org.tiatus.dao;

import org.tiatus.entity.Event;

import java.util.List;

/**
 * Created by johnreynolds on 10/10/2016.
 */
public interface EventDao {
    /**
     * Add event
     * @param event to create
     * @return created event
     * @throws DaoException on error
     */
    Event addEvent(Event event) throws DaoException;

    /**
     * Remove a event
     * @param event to remove
     * @throws DaoException on error
     */
    void deleteEvent(Event event) throws DaoException;

    /**
     * Update a Event
     * @param event Event to update
     * @return updated event
     * @throws DaoException on error
     */
    Event updateEvent(Event event) throws DaoException;

    /**
     * Get events
     * @return list of events
     */
    List<Event> getEvents();

    /**
     * Get unassigned events
     * @return list of events assigned not yet to a race
     */
    List<Event> getUnassignedEvents();
}
