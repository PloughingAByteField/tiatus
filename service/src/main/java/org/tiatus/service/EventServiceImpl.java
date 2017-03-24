package org.tiatus.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.dao.DaoException;
import org.tiatus.dao.EventDao;
import org.tiatus.dao.RaceEventDao;
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

    private static final String DAO_EXCEPTION = "Got dao exception: ";
    private final EventDao dao;
    private final RaceEventDao raceEventDao;

    /**
     * Constructor for service
     * @param dao Event dao object injected by cdi
     * @param raceEventDao RaceEvent dao object injected by cdi
     */
    @Inject
    public EventServiceImpl(EventDao dao, RaceEventDao raceEventDao) {
        this.dao = dao;
        this.raceEventDao = raceEventDao;
    }

    @Override
    public Event addEvent(Event event) throws ServiceException {
        LOG.debug("Adding event " + event);
        try {
            return dao.addEvent(event);

        } catch (DaoException e) {
            LOG.warn(DAO_EXCEPTION);
            throw new ServiceException(e);
        }
    }

    @Override
    public RaceEvent addRaceEvent(RaceEvent raceEvent) throws ServiceException {
        LOG.debug("Adding raceEvent " + raceEvent);
        try {
            return raceEventDao.addRaceEvent(raceEvent);

        } catch (DaoException e) {
            LOG.warn(DAO_EXCEPTION);
            throw new ServiceException(e);
        }
    }

    @Override
    public Event updateEvent(Event event) throws ServiceException {
        LOG.debug("Updating event " + event);
        try {
            return dao.updateEvent(event);

        } catch (DaoException e) {
            LOG.warn(DAO_EXCEPTION);
            throw new ServiceException(e);
        }
    }

    @Override
    public void deleteEvent(Event event) throws ServiceException {
        LOG.debug("Delete event " + event.getId());
        try {
            dao.deleteEvent(event);
        } catch (DaoException e) {
            LOG.warn(DAO_EXCEPTION);
            throw new ServiceException(e);
        }
    }

    @Override
    public void deleteRaceEvent(RaceEvent raceEvent) throws ServiceException {
        LOG.debug("Delete raceEvent " + raceEvent.getId());
        try {
            raceEventDao.deleteRaceEvent(raceEvent);
        } catch (DaoException e) {
            LOG.warn(DAO_EXCEPTION);
            throw new ServiceException(e);
        }
    }

    @Override
    public void updateRaceEvents(List<RaceEvent> raceEvents) throws ServiceException {
        // find event by id, get id set race order and update
        try {
            for (RaceEvent raceEventToUpdate : raceEvents) {
                RaceEvent existingRaceEvent = raceEventDao.getRaceEventByEvent(raceEventToUpdate.getEvent());
                if (existingRaceEvent != null) {
                    existingRaceEvent.setRaceEventOrder(raceEventToUpdate.getRaceEventOrder());
                    raceEventDao.updateRaceEvent(existingRaceEvent);
                }
            }
        } catch (DaoException e) {
            LOG.warn(DAO_EXCEPTION);
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Event> getEvents() {
        return dao.getEvents();
    }

    @Override
    public List<RaceEvent> getAssignedEvents() {
        return raceEventDao.getRaceEvents();
    }

    @Override
    public List<Event> getUnassignedEvents() {
        return dao.getUnassignedEvents();
    }
}
