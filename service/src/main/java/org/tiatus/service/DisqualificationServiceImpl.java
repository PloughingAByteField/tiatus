package org.tiatus.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.tiatus.dao.DaoException;
import org.tiatus.dao.DisqualificationDao;
import org.tiatus.entity.Disqualification;

import jakarta.jms.JMSException;

import java.util.List;

/**
 * Created by johnreynolds on 01/03/2017.
 */
@Service
public class DisqualificationServiceImpl implements DisqualificationService {
    private static final Logger LOG = LoggerFactory.getLogger(DisqualificationServiceImpl.class);

    protected final static String CACHE_LIST_NAME = "disqualifications";
    protected final static String CACHE_ENTRY_NAME = "disqualification";

    @Autowired
    protected DisqualificationDao dao;

    @Autowired
    protected MessageSenderService sender;

    @Autowired
    protected CacheManager cacheManager;

    @Override
    public Disqualification addDisqualification(Disqualification disqualification, String sessionId) throws ServiceException {
        LOG.debug("Adding disqualification for entry " + disqualification.getEntry().getId());
        try {
            Disqualification q = dao.addDisqualification(disqualification);
            Message message = Message.createMessage(q, MessageType.ADD, sessionId);
            sender.sendMessage(message);
            clearCache();

            return q;

        } catch (DaoException e) {
            LOG.warn("Got dao exception");
            throw new ServiceException(e);

        } catch (JMSException e) {
            LOG.warn("Got jms exception", e);
            throw new ServiceException(e);
        }
    }

    @Override
    public void deleteDisqualification(Disqualification disqualification, String sessionId) throws ServiceException {
        LOG.debug("Delete disqualification " + disqualification.getId());
        try {
            dao.removeDisqualification(disqualification);
            Message message = Message.createMessage(disqualification, MessageType.DELETE, sessionId);
            sender.sendMessage(message);
            updateCache(disqualification);

        } catch (DaoException e) {
            LOG.warn("Got dao exception");
            throw new ServiceException(e);

        } catch (JMSException e) {
            LOG.warn("Got jms exception", e);
            throw new ServiceException(e);
        }
    }

    @Override
    public Disqualification updateDisqualification(Disqualification disqualification, String sessionId) throws ServiceException {
        LOG.debug("Update disqualification " + disqualification.getId());
        try {
            Disqualification updated = dao.updateDisqualification(disqualification);
            Message message = Message.createMessage(updated, MessageType.UPDATE, sessionId);
            sender.sendMessage(message);
            updateCache(disqualification);

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
    public Disqualification getDisqualificationForId(Long id) {
        return dao.getDisqualificationForId(id);
    }

    @Override
    @Cacheable(value = CACHE_LIST_NAME)
    public List<Disqualification> getDisqualifications() {
        return dao.getDisqualifications();
    }

    private void updateCache(Disqualification disqualification) {
        Cache cache = cacheManager.getCache(CACHE_ENTRY_NAME);
        if (cache != null) {
            cache.evictIfPresent(disqualification.getId().longValue());
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
