package org.tiatus.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.tiatus.dao.DaoException;
import org.tiatus.dao.ClubDao;
import org.tiatus.entity.Club;

import jakarta.jms.JMSException;

import java.util.List;

/**
 * Created by johnreynolds on 25/06/2016.
 */
@Service
public class ClubServiceImpl implements ClubService {

    private static final Logger LOG = LoggerFactory.getLogger(ClubServiceImpl.class);

    protected final static String CACHE_LIST_NAME = "clubs";
    protected final static String CACHE_ENTRY_NAME = "club";

    @Autowired
    protected ClubDao dao;

    @Autowired
    protected MessageSenderService sender;

    @Autowired
    protected CacheManager cacheManager;

    @Override
    public Club addClub(Club club, String sessionId) throws ServiceException {
        LOG.debug("Adding club " + club);
        try {
            Club c = dao.addClub(club);
            Message message = Message.createMessage(club, MessageType.ADD, sessionId);
            sender.sendMessage(message);
            clearCache();

            return c;

        } catch (DaoException e) {
            LOG.warn("Got dao exception");
            throw new ServiceException(e);
            
        } catch (JMSException e) {
            LOG.warn("Got jms exception", e);
            throw new ServiceException(e);
        }
    }

    @Override
    public void deleteClub(Club club, String sessionId) throws ServiceException {
        LOG.debug("Delete club " + club.getId());
        try {
            dao.removeClub(club);
            Message message = Message.createMessage(club, MessageType.DELETE, sessionId);
            sender.sendMessage(message);
            updateCache(club);

        } catch (DaoException e) {
            LOG.warn("Got dao exception");
            throw new ServiceException(e);

        } catch (JMSException e) {
            LOG.warn("Got jms exception", e);
            throw new ServiceException(e);
        }
    }

    @Override
    public Club updateClub(Club club, String sessionId) throws ServiceException {
        LOG.debug("Update club " + club.getId());
        try {
            Club updated = dao.updateClub(club);
            Message message = Message.createMessage(updated, MessageType.UPDATE, sessionId);
            sender.sendMessage(message);
            updateCache(club);

            return updated;

        } catch (DaoException e) {
            LOG.warn("Got dao exception");
            throw new ServiceException(e);

        } catch (JMSException e) {
            LOG.warn("Got jms exception", e);
            throw new ServiceException(e);
        }
    }

    @Override
    @Cacheable(value = CACHE_ENTRY_NAME, key = "#id")
    public Club getClubForId(Long id) {
        return dao.getClubForId(id);
    }

    @Override
    @Cacheable(value = CACHE_LIST_NAME)
    public List<Club> getClubs() {
        return dao.getClubs();
    }

    private void updateCache(Club club) {
        Cache cache = cacheManager.getCache(CACHE_ENTRY_NAME);
        if (cache != null) {
            cache.evictIfPresent(club.getId().longValue());
        }

        clearCache();
    }

    private void clearCache() {
        Cache cache = cacheManager.getCache(CACHE_LIST_NAME);
        if (cache != null) {
            cache.clear();
        }
    }
}
