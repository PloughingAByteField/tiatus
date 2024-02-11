package org.tiatus.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
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

    protected final static String CACHE_LIST_NAME = "events";
    protected final static String CACHE_ENTRY_NAME = "event";

    @Autowired
    protected EventDao dao;

    @Autowired
    protected RaceEventDao raceEventDao;

    @Autowired
    protected MessageSenderService sender;

    @Autowired
    protected CacheManager cacheManager;

    @Override
    public Event addEvent(Event event, String sessionId) throws ServiceException {
        LOG.debug("Adding event " + event);
        try {
            Event newEvent = dao.addEvent(event);
            Message message = Message.createMessage(newEvent, MessageType.ADD, sessionId);
            sender.sendMessage(message);
            clearCache();

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
            clearRaceEventCache();

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
            updateCache(event);

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
            updateCache(event);

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
            updateRaceEventCache(raceEvent);

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
                    updateRaceEventCache(existingRaceEvent);
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
    @Cacheable(value = CACHE_LIST_NAME)
    public List<Event> getEvents() {
        return dao.getEvents();
    }

    @Override
    @Cacheable(value = "assigned_" + CACHE_LIST_NAME)
    public List<RaceEvent> getAssignedEvents() {
        return raceEventDao.getRaceEvents();
    }

    @Override
    @Cacheable(value = "unassigned_" + CACHE_LIST_NAME)
    public List<Event> getUnassignedEvents() {
        return dao.getUnassignedEvents();
    }

    @Override
    @Cacheable(value = CACHE_ENTRY_NAME, key = "#id")
    public Event getEventForId(Long id) {
        return dao.getEventForId(id);
    }

    @Override
    @Cacheable(value = CACHE_ENTRY_NAME + "_for_race", key = "#id")
    public RaceEvent getRaceEventForId(Long id) {
        return raceEventDao.getRaceEventForId(id);
    }

    @Override
    @Cacheable(value = "race_" + CACHE_ENTRY_NAME, key = "#race.id")
    public List<RaceEvent> getRaceEventsForRace(Race race) {
        return raceEventDao.getRaceEventsForRace(race);
    }

    private void updateCache(Event event) {
        Cache cache = cacheManager.getCache(CACHE_ENTRY_NAME);
        if (cache != null) {
            cache.evictIfPresent(event.getId().longValue());
        }

        clearCache();
    }

    private void updateRaceEventCache(RaceEvent event) {
        Cache cache = cacheManager.getCache("race_" + CACHE_ENTRY_NAME);
        if (cache != null) {
            cache.evictIfPresent(event.getId().longValue());
        }

        cache = cacheManager.getCache(CACHE_ENTRY_NAME + "_for_race");
        if (cache != null) {
            cache.evictIfPresent(event.getRace().getId().longValue());
        }

        clearRaceEventCache();
    }

    private void clearRaceEventCache() {
        Cache cache = cacheManager.getCache("assigned_" + CACHE_LIST_NAME);
        if (cache != null) {
            cache.clear();
        }

        cache = cacheManager.getCache("unassigned_" + CACHE_LIST_NAME);
        if (cache != null) {
            cache.clear();
        }
        
    }

    private void clearCache() {
        Cache cache = cacheManager.getCache(CACHE_LIST_NAME);
        if (cache != null) {
            cache.clear();
        }
    }
}
