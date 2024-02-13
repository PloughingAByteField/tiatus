package org.tiatus.service;

import org.tiatus.dao.DaoException;
import org.tiatus.dao.EntryPositionTimeDao;
import org.tiatus.entity.*;

import jakarta.jms.JMSException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TimesServiceImpl implements TimesService {

    private static final Logger LOG = LoggerFactory.getLogger(TimesServiceImpl.class);

    protected final static String CACHE_LIST_NAME = "position_times";
    protected final static String ALL_CACHE_LIST_NAME = "all_position_times";
    protected final static String FOR_POSITION_CACHE_LIST_NAME = "position_times_for_position";

    @Autowired
    protected EntryPositionTimeDao dao;

    @Autowired
    protected MessageSenderService sender;

    @Autowired
    protected CacheManager cacheManager;

    @Override
    public EntryPositionTime createTime(EntryPositionTime entryPositionTime, String sessionId) throws ServiceException {
        try {
            EntryPositionTime newTime = dao.createTime(entryPositionTime);
            Message message = Message.createMessage(newTime, MessageType.ADD, sessionId);
            sender.sendMessage(message);
            clearCache(newTime);

            return newTime;

        } catch (DaoException e) {
            LOG.warn("Got dao exception");
            throw new ServiceException(e);
            
        } catch (JMSException e) {
            LOG.warn("Got jms exception", e);
            throw new ServiceException(e);
        }
    }

    @Override
    public EntryPositionTime updateTime(EntryPositionTime entryPositionTime, String sessionId) throws ServiceException {
        try {
            EntryPositionTime updatedTime = dao.updateTime(entryPositionTime);
            Message message = Message.createMessage(updatedTime, MessageType.UPDATE, sessionId);
            sender.sendMessage(message);
            clearCache(entryPositionTime);

            return updatedTime;
            
        } catch (DaoException e) {
            LOG.warn("Got dao exception");
            throw new ServiceException(e);
            
        } catch (JMSException e) {
            LOG.warn("Got jms exception", e);
            throw new ServiceException(e);
        }
    }

    @Override
    public List<EntryPositionTime> getPositionTimesForPositionInRace(Race race, Position position) throws ServiceException {
        RacePosition racePosition = new RacePosition();
        racePosition.setRace(race);
        racePosition.setPosition(position);
        return getPositionTimesForPositionInRace(racePosition);
    }

    @Cacheable(value = FOR_POSITION_CACHE_LIST_NAME, key = "#racePosition.hashCode")
    private List<EntryPositionTime>  getPositionTimesForPositionInRace(RacePosition racePosition) throws ServiceException {
        LOG.debug("Get list of times for race " + racePosition.getRace().getName() + " at position " + racePosition.getPosition().getName());
        try {
            return dao.getPositionTimesForPositionInRace(racePosition.getRace(), racePosition.getPosition());

        } catch (DaoException e) {
            LOG.warn("Got dao exception");
            throw new ServiceException(e);
        }
    }

    @Override
    @Cacheable(value = ALL_CACHE_LIST_NAME, key = "#race.id")
    public List<EntryPositionTime> getAllTimesForRace(Race race) throws ServiceException {
        LOG.debug("Get list of times for race " + race.getName());
        try {
            return dao.getAllTimesForRace(race);

        } catch (DaoException e) {
            LOG.warn("Got dao exception");
            throw new ServiceException(e);
        }
    }

    @Override
    @Cacheable(value = CACHE_LIST_NAME, key = "#race.id")
    public List<EntryPositionTime> getTimesForRace(Race race) throws ServiceException {
        LOG.debug("Get full list of times for race " + race.getName());
        try {
            return dao.getTimesForRace(race);

        } catch (DaoException e) {
            LOG.warn("Got dao exception");
            throw new ServiceException(e);
        }
    }

    @Override
    public void clearCaches() {
        Cache cache = cacheManager.getCache(CACHE_LIST_NAME);
        if (cache != null) {
            cache.clear();
        }

        cache = cacheManager.getCache(ALL_CACHE_LIST_NAME);
        if (cache != null) {
            cache.clear();
        }

        cache = cacheManager.getCache(FOR_POSITION_CACHE_LIST_NAME);
        if (cache != null) {
            cache.clear();
        }
    }

    private void clearCache(EntryPositionTime entryPositionTime) {
        Cache cache = cacheManager.getCache(CACHE_LIST_NAME);
        if (cache != null) {
            cache.evictIfPresent(entryPositionTime.getEntry().getRace().getId().longValue());
        }

        cache = cacheManager.getCache(ALL_CACHE_LIST_NAME);
        if (cache != null) {
            cache.evictIfPresent(entryPositionTime.getEntry().getRace().getId().longValue());
        }

        cache = cacheManager.getCache(FOR_POSITION_CACHE_LIST_NAME);
        if (cache != null) {
            RacePosition racePosition = new RacePosition();
            racePosition.setRace(entryPositionTime.getEntry().getRace());
            racePosition.setPosition(entryPositionTime.getPosition());
            cache.evictIfPresent(racePosition.hashCode());
        }
    }
}
