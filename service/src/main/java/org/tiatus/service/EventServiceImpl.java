package org.tiatus.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tiatus.dao.DaoException;
import org.tiatus.dao.EventDao;
import org.tiatus.dao.RaceEventDao;
import org.tiatus.entity.Event;
import org.tiatus.entity.Race;
import org.tiatus.entity.RaceEvent;

import jakarta.jms.JMSException;

import java.util.List;

/**
 * Created by johnreynolds on 10/10/2016.
 */
@Service
public class EventServiceImpl implements EventService {
    private static final Logger LOG = LoggerFactory.getLogger(EventServiceImpl.class);

    private static final String DAO_EXCEPTION = "Got dao exception: ";
    private static final String JMS_EXCEPTION = "Got jms exception: ";

    @Autowired
    protected EventDao dao;

    @Autowired
    protected RaceEventDao raceEventDao;

    @Autowired
    protected MessageSenderService sender;

    @Override
    public Event addEvent(Event event, String sessionId) throws ServiceException {
        LOG.debug("Adding event " + event);
        try {
            Event newEvent = dao.addEvent(event);
            Message message = Message.createMessage(newEvent, MessageType.ADD, sessionId);
            sender.sendMessage(message);
            return newEvent;

        } catch (DaoException e) {
            LOG.warn(DAO_EXCEPTION);
            throw new ServiceException(e);
            
        } catch (JMSException e) {
            LOG.warn(JMS_EXCEPTION, e);
            throw new ServiceException(e);
        }
    }

    @Override
    public RaceEvent addRaceEvent(RaceEvent raceEvent, String sessionId) throws ServiceException {
        LOG.debug("Adding raceEvent " + raceEvent);
        try {
            RaceEvent newRaceEvent = raceEventDao.addRaceEvent(raceEvent);
            Message message = Message.createMessage(newRaceEvent, MessageType.ADD, sessionId);
            sender.sendMessage(message);
            return newRaceEvent;

        } catch (DaoException e) {
            LOG.warn(DAO_EXCEPTION);
            throw new ServiceException(e);

        } catch (JMSException e) {
            LOG.warn(JMS_EXCEPTION, e);
            throw new ServiceException(e);
        }
    }

    @Override
    public Event updateEvent(Event event, String sessionId) throws ServiceException {
        LOG.debug("Updating event " + event);
        try {
            Event updated =  dao.updateEvent(event);
            Message message = Message.createMessage(updated, MessageType.UPDATE, sessionId);
            sender.sendMessage(message);
            return updated;

        } catch (DaoException e) {
            LOG.warn(DAO_EXCEPTION);
            throw new ServiceException(e);

        } catch (JMSException e) {
            LOG.warn(JMS_EXCEPTION, e);
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
            LOG.warn(JMS_EXCEPTION, e);
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
            LOG.warn(JMS_EXCEPTION, e);
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
            LOG.warn(JMS_EXCEPTION, e);
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

    @Override
    public Event getEventForId(Long id) {
        return dao.getEventForId(id);
    }

    @Override
    public RaceEvent getRaceEventForId(Long id) {
        return raceEventDao.getRaceEventForId(id);
    }

    @Override
    public List<RaceEvent> getRaceEventsForRace(Race race) {
        return raceEventDao.getRaceEventsForRace(race);
    }
}
