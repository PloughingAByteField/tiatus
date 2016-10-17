package org.tiatus.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.dao.DaoException;
import org.tiatus.dao.EventDao;
import org.tiatus.entity.Event;
import org.tiatus.entity.RaceEvent;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.util.List;

/**
 * Created by johnreynolds on 10/10/2016.
 */
@Default
public class EventServiceImpl implements EventService {
    private static final Logger LOG = LoggerFactory.getLogger(EventServiceImpl.class);

    private final EventDao dao;

    /**
     * Constructor for service
     * @param dao object injected by cdi
     */
    @Inject
    public EventServiceImpl(EventDao dao) {
        this.dao = dao;
    }

    @Override
    public Event addEvent(Event event) throws ServiceException {
        LOG.debug("Adding event " + event);
        try {
            return dao.addEvent(event);

        } catch (DaoException e) {
            LOG.warn("Got dao exception");
            throw new ServiceException(e);
        }
    }

    @Override
    public void deleteEvent(Event event) throws ServiceException {
        LOG.debug("Delete event " + event.getId());
        try {
            dao.deleteEvent(event);
        } catch (DaoException e) {
            LOG.warn("Got dao exception");
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Event> getEvents() {
        return dao.getEvents();
    }

    @Override
    public List<RaceEvent> getAssignedEvents() {
        return dao.getAssignedEvents();
    }

    @Override
    public List<Event> getUnassignedEvents() {
        return dao.getUnassignedEvents();
    }
}
