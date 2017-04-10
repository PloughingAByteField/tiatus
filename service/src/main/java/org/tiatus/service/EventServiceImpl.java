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
import javax.jms.JMSException;
import java.util.List;

/**
 * Created by johnreynolds on 10/10/2016.
 */
@Default
public class EventServiceImpl implements EventService {
    private static final Logger LOG = LoggerFactory.getLogger(EventServiceImpl.class);

    private static final String DAO_EXCEPTION = "Got dao exception: ";
    private static final String JMS_EXCEPTION = "Got jms exception: ";
    private final EventDao dao;
    private final RaceEventDao raceEventDao;
    private MessageSenderService sender;

    /**
     * Constructor for service
     * @param dao Event dao object injected by cdi
     * @param raceEventDao RaceEvent dao object injected by cdi
     */
    @Inject
    public EventServiceImpl(EventDao dao, RaceEventDao raceEventDao, MessageSenderService sender) {
        this.dao = dao;
        this.raceEventDao = raceEventDao;
        this.sender = sender;
    }

    @Override
    public Event addEvent(Event event, String sessionId) throws ServiceException {
        LOG.debug("Adding event " + event);
        try {
            Event daoEvent = dao.addEvent(event);
            Message message = Message.createMessage(daoEvent, MessageType.ADD, sessionId);
            sender.sendMessage(message);
            return daoEvent;

        } catch (DaoException e) {
            LOG.warn(DAO_EXCEPTION);
            throw new ServiceException(e);
        } catch (JMSException e) {
            LOG.warn(DAO_EXCEPTION, e);
            throw new ServiceException(e);
        }
    }

    @Override
    public RaceEvent addRaceEvent(RaceEvent raceEvent, String sessionId) throws ServiceException {
        LOG.debug("Adding raceEvent " + raceEvent);
        try {
            RaceEvent daoRaceEvent = raceEventDao.addRaceEvent(raceEvent);
            Message message = Message.createMessage(daoRaceEvent, MessageType.ADD, sessionId);
            sender.sendMessage(message);
            return daoRaceEvent;

        } catch (DaoException e) {
            LOG.warn(DAO_EXCEPTION);
            throw new ServiceException(e);
        } catch (JMSException e) {
            LOG.warn(DAO_EXCEPTION, e);
            throw new ServiceException(e);
        }
    }

    @Override
    public Event updateEvent(Event event, String sessionId) throws ServiceException {
        LOG.debug("Updating event " + event);
        try {
            Event daoEvent =  dao.updateEvent(event);
            Message message = Message.createMessage(daoEvent, MessageType.UPDATE, sessionId);
            sender.sendMessage(message);
            return daoEvent;

        } catch (DaoException e) {
            LOG.warn(DAO_EXCEPTION);
            throw new ServiceException(e);
        } catch (JMSException e) {
            LOG.warn(DAO_EXCEPTION, e);
            throw new ServiceException(e);
        }
    }

    @Override
    public void deleteEvent(Event event, String sessionId) throws ServiceException {
        LOG.debug("Delete event " + event.getId());
        try {
            dao.deleteEvent(event);
            Message message = Message.createMessage(event, MessageType.DELETE, sessionId);
            sender.sendMessage(message);

        } catch (DaoException e) {
            LOG.warn(DAO_EXCEPTION);
            throw new ServiceException(e);
        } catch (JMSException e) {
            LOG.warn(DAO_EXCEPTION, e);
            throw new ServiceException(e);
        }
    }

    @Override
    public void deleteRaceEvent(RaceEvent raceEvent, String sessionId) throws ServiceException {
        LOG.debug("Delete raceEvent " + raceEvent.getId());
        try {
            raceEventDao.deleteRaceEvent(raceEvent);
            Message message = Message.createMessage(raceEvent, MessageType.UPDATE, sessionId);
            sender.sendMessage(message);

        } catch (DaoException e) {
            LOG.warn(DAO_EXCEPTION);
            throw new ServiceException(e);
        } catch (JMSException e) {
            LOG.warn(DAO_EXCEPTION, e);
            throw new ServiceException(e);
        }
    }

    @Override
    public void updateRaceEvents(List<RaceEvent> raceEvents, String sessionId) throws ServiceException {
        // find event by id, get id set race order and update
        try {
            for (RaceEvent raceEventToUpdate : raceEvents) {
                RaceEvent existingRaceEvent = raceEventDao.getRaceEventByEvent(raceEventToUpdate.getEvent());
                if (existingRaceEvent != null) {
                    existingRaceEvent.setRaceEventOrder(raceEventToUpdate.getRaceEventOrder());
                    raceEventDao.updateRaceEvent(existingRaceEvent);
                    Message message = Message.createMessage(existingRaceEvent, MessageType.UPDATE, sessionId);
                    sender.sendMessage(message);
                }
            }
        } catch (DaoException e) {
            LOG.warn(DAO_EXCEPTION);
            throw new ServiceException(e);
        } catch (JMSException e) {
            LOG.warn(DAO_EXCEPTION, e);
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
