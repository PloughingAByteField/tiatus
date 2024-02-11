package org.tiatus.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.tiatus.dao.DaoException;
import org.tiatus.dao.RacePositionTemplateDao;
import org.tiatus.entity.RacePositionTemplate;
import org.tiatus.entity.RacePositionTemplateEntry;

import jakarta.jms.JMSException;

import java.util.List;

/**
 * Created by johnreynolds on 09/03/2017.
 */
@Service
public class RacePositionTemplateServiceImpl implements RacePositionTemplateService {
    
    private static final Logger LOG = LoggerFactory.getLogger(RacePositionTemplateServiceImpl.class);

    protected final static String CACHE_LIST_NAME = "position_templates";
    protected final static String CACHE_ENTRY_NAME = "position_template";

    @Autowired
    protected RacePositionTemplateDao dao;

    @Autowired
    protected MessageSenderService sender;

    @Autowired
    protected CacheManager cacheManager;    

    @Override
    public RacePositionTemplate addRacePositionTemplate(RacePositionTemplate template, String sessionId) throws ServiceException {
        LOG.debug("Adding template " + template.getName());
        try {
            RacePositionTemplate rpt = dao.addRacePositionTemplate(template);
            Message message = Message.createMessage(rpt, MessageType.ADD, sessionId);
            sender.sendMessage(message);
            clearCache();

            return rpt;

        } catch (DaoException e) {
            LOG.warn("Got dao exception");
            throw new ServiceException(e);
            
        } catch (JMSException e) {
            LOG.warn("Got jms exception", e);
            throw new ServiceException(e);
        }
    }

    @Override
    public void deleteRacePositionTemplate(RacePositionTemplate template, String sessionId) throws ServiceException {
        LOG.debug("Delete template " + template.getId());
        try {
            dao.deleteRacePositionTemplate(template);
            Message message = Message.createMessage(template, MessageType.DELETE, sessionId);
            sender.sendMessage(message);
            updateCache(template);

        } catch (DaoException e) {
            LOG.warn("Got dao exception");
            throw new ServiceException(e);

        } catch (JMSException e) {
            LOG.warn("Got jms exception", e);
            throw new ServiceException(e);
        }
    }

    @Override
    public RacePositionTemplate updateRacePositionTemplate(RacePositionTemplate template, String sessionId) throws ServiceException {
        LOG.debug("Update template " + template.getId());
        try {
            RacePositionTemplate updated = dao.updateRacePositionTemplate(template);
            Message message = Message.createMessage(updated, MessageType.UPDATE, sessionId);
            sender.sendMessage(message);
            updateCache(template);

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
    @Cacheable(value = CACHE_LIST_NAME)
    public List<RacePositionTemplate> getRacePositionTemplates() {
        return dao.getRacePositionTemplates();
    }

    @Override
    @Cacheable(value = CACHE_ENTRY_NAME, key = "#id")
    public RacePositionTemplate getTemplateForId(Long id) {
        return dao.getTemplateForId(id);
    }

    @Override
    public RacePositionTemplateEntry addTemplateEntry(RacePositionTemplateEntry entry, String sessionId) throws ServiceException {
        try {
            RacePositionTemplateEntry rpte = dao.addTemplateEntry(entry);
            Message message = Message.createMessage(rpte, MessageType.ADD, sessionId);
            sender.sendMessage(message);
            updateCache(entry.getTemplate());

            return rpte;

        } catch (DaoException e) {
            LOG.warn("Got dao exception");
            throw new ServiceException(e);

        } catch (JMSException e) {
            LOG.warn("Got jms exception", e);
            throw new ServiceException(e);
        }
    }

    @Override
    public void deleteTemplateEntry(RacePositionTemplateEntry entry, String sessionId) throws ServiceException {
        try {
            dao.deleteTemplateEntry(entry);
            Message message = Message.createMessage(entry, MessageType.DELETE, sessionId);
            sender.sendMessage(message);
            updateCache(entry.getTemplate());

        } catch (DaoException e) {
            LOG.warn("Got dao exception");
            throw new ServiceException(e);

        } catch (JMSException e) {
            LOG.warn("Got jms exception", e);
            throw new ServiceException(e);
        }
    }

    @Override
    public RacePositionTemplateEntry updateTemplateEntry(RacePositionTemplateEntry entry, String sessionId) throws ServiceException {
        try {
            RacePositionTemplateEntry updated = dao.updateTemplateEntry(entry);
            Message message = Message.createMessage(updated, MessageType.UPDATE, sessionId);
            sender.sendMessage(message);
            updateCache(entry.getTemplate());

            return updated;

        } catch (DaoException e) {
            LOG.warn("Got dao exception");
            throw new ServiceException(e);
            
        } catch (JMSException e) {
            LOG.warn("Got jms exception", e);
            throw new ServiceException(e);
        }
    }

    private void updateCache(RacePositionTemplate template) {
        Cache cache = cacheManager.getCache(CACHE_ENTRY_NAME);
        if (cache != null) {
            cache.evictIfPresent(template.getId().longValue());
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
