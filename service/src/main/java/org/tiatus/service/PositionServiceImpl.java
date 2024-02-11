package org.tiatus.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.tiatus.dao.DaoException;
import org.tiatus.dao.PositionDao;
import org.tiatus.entity.Position;

import jakarta.jms.JMSException;

import java.util.List;

/**
 * Created by johnreynolds on 25/06/2016.
 */
@Service
public class PositionServiceImpl implements PositionService {

    private static final Logger LOG = LoggerFactory.getLogger(PositionServiceImpl.class);

    protected final static String CACHE_LIST_NAME = "positions";
    protected final static String CACHE_ENTRY_NAME = "position";

    @Autowired
    protected PositionDao dao;

    @Autowired
    protected MessageSenderService sender;

    @Autowired
    protected CacheManager cacheManager;

    @Override
    public Position addPosition(Position position, String sessionId) throws ServiceException {
        LOG.debug("Adding position " + position);
        try {
            Position newPosition = dao.addPosition(position);
            Message message = Message.createMessage(newPosition, MessageType.ADD, sessionId);
            sender.sendMessage(message);
            clearCache();

            return newPosition;

        } catch (DaoException e) {
            LOG.warn("Got dao exception");
            throw new ServiceException(e);
            
        } catch (JMSException e) {
            LOG.warn("Got jms exception", e);
            throw new ServiceException(e);
        }
    }

    @Override
    public void removePosition(Position position, String sessionId) throws ServiceException {
        LOG.debug("Delete position " + position.getId());
        try {
            dao.removePosition(position);
            Message message = Message.createMessage(position, MessageType.DELETE, sessionId);
            sender.sendMessage(message);
            updateCache(position);

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
    public Position getPositionForId(Long id) {
        return dao.getPositionForId(id);
    }

    @Override
    @Cacheable(value = CACHE_LIST_NAME)
    public List<Position> getPositions() {
        return dao.getPositions();
    }

    @Override
    public void updatePosition(Position position, String sessionId) throws ServiceException {
        try {
            Position updated = dao.updatePosition(position);
            Message message = Message.createMessage(updated, MessageType.UPDATE, sessionId);
            sender.sendMessage(message);
            updateCache(position);

        } catch (DaoException e) {
            LOG.warn("Got dao exception");
            throw new ServiceException(e);
            
        } catch (JMSException e) {
            LOG.warn("Got jms exception", e);
            throw new ServiceException(e);
        }
    }

    private void updateCache(Position position) {
        Cache cache = cacheManager.getCache(CACHE_ENTRY_NAME);
        if (cache != null) {
            cache.evictIfPresent(position.getId().longValue());
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
