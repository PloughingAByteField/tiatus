package org.tiatus.service;

import org.tiatus.entity.Event;

import java.util.List;

/**
 * Created by johnreynolds on 25/06/2016.
 */
public interface EventService {
    /**
     * Add event
     * @param event to create
     * @return created event
     * @throws ServiceException on error
     */
    Event addEvent(Event event) throws ServiceException;

    /**
     * Remove a event
     * @param event to remove
     * @throws ServiceException on error
     */
    void deleteEvent(Event event) throws ServiceException;

    /**
     * Get events
     * @return list of events
     */
    List<Event> getEvents();
}
