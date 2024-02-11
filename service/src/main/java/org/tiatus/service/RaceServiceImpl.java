package org.tiatus.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.tiatus.dao.DaoException;
import org.tiatus.dao.RaceDao;
import org.tiatus.entity.Race;

import jakarta.jms.JMSException;

import java.util.List;

/**
 * Created by johnreynolds on 25/06/2016.
 */
@Service
public class RaceServiceImpl implements RaceService {

    private static final Logger LOG = LoggerFactory.getLogger(RaceServiceImpl.class);

    @Autowired
    protected RaceDao dao;

    @Autowired
    protected MessageSenderService sender;

    @Autowired
    protected CacheManager cacheManager;

    @Override
    @Cacheable(value = "race", key = "#id")
    public Race getRaceForId(Long id) {
        return dao.getRaceForId(id);
    }

    @Override
    public Race addRace(Race race, String sessionId) throws ServiceException {
        LOG.debug("Adding race " + race);
        try {
            Race newRace = dao.addRace(race);
            Message message = Message.createMessage(newRace, MessageType.ADD, sessionId);
            sender.sendMessage(message);
            cacheManager.getCache("races").clear();
            return newRace;

        } catch (DaoException e) {
            LOG.warn("Got dao exception");
            throw new ServiceException(e);
            
        } catch (JMSException e) {
            LOG.warn("Got jms exception");
            throw new ServiceException(e);
        }
    }

    @Override
    public void deleteRace(Race race, String sessionId) throws ServiceException {
        LOG.debug("Delete race " + race.getId());
        try {
            dao.removeRace(race);
            Message message = Message.createMessage(race, MessageType.DELETE, sessionId);
            sender.sendMessage(message);
            cacheManager.getCache("race").evictIfPresent(race.getId());
            cacheManager.getCache("races").clear();

        } catch (DaoException e) {
            LOG.warn("Got dao exception");
            throw new ServiceException(e);

        } catch (JMSException e) {
            LOG.warn("Got jms exception");
            throw new ServiceException(e);
        }
    }

    @Override
    public Race updateRace(Race race, String sessionId) throws ServiceException {
        LOG.debug("Update race " + race.getId());
        try {
            Race updated = dao.updateRace(race);
            Message message = Message.createMessage(updated, MessageType.UPDATE, sessionId);
            sender.sendMessage(message);
            cacheManager.getCache("race").evictIfPresent(race.getId());
            cacheManager.getCache("races").clear();

            return updated;

        } catch (DaoException e) {
            LOG.warn("Got dao exception");
            throw new ServiceException(e);

        } catch (JMSException e) {
            LOG.warn("Got jms exception");
            throw new ServiceException(e);
        }
    }

    @Override
    @Cacheable(value = "races")
    public List<Race> getRaces() {
        return dao.getRaces();
    }
}
