package org.tiatus.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.tiatus.dao.DaoException;
import org.tiatus.dao.RaceDao;
import org.tiatus.entity.Race;

import jakarta.jms.JMSException;

import java.io.File;
import java.util.List;

/**
 * Created by johnreynolds on 25/06/2016.
 */
@Service
public class RaceServiceImpl implements RaceService {

    private static final Logger LOG = LoggerFactory.getLogger(RaceServiceImpl.class);

    protected final static String CACHE_LIST_NAME = "races";
    protected final static String CACHE_ENTRY_NAME = "race";

    @Autowired
    protected RaceDao dao;

    @Autowired
    private EntryService entriesService;

    @Autowired
    protected MessageSenderService sender;

    @Autowired
    protected CacheManager cacheManager;

    @Autowired
    protected ReportService reportService;

    @Autowired
    protected Environment environment;

    @Override
    @Cacheable(value = CACHE_ENTRY_NAME, key = "#id")
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
            clearCache();

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
            entriesService.deleteEntriesForRace(race);

            dao.removeRace(race);
            Message message = Message.createMessage(race, MessageType.DELETE, sessionId);
            sender.sendMessage(message);
            updateCache(race);

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
            updateCache(race);

            if (race.isClosed()) {
                reportService.createReportForRace(race);
            }

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
    @Cacheable(value = CACHE_LIST_NAME)
    public List<Race> getRaces() {
        return dao.getRaces();
    }

    @Override
    public boolean areRacePDFResultsReady(Race race) {

        String fileName = "/tiatus/results/" + race.getName() + "_ByEvent.pdf";
        File resultsFile = new File(environment.getProperty("tiatus.files") + fileName);
        if (!resultsFile.exists()) {
            return false;
        }

        fileName = "/tiatus/results/" + race.getName() + "_ByTime.pdf";
        resultsFile = new File(environment.getProperty("tiatus.files") + fileName);
        return resultsFile.exists();
    }
    
    private void updateCache(Race race) {
        Cache cache = cacheManager.getCache(CACHE_ENTRY_NAME);
        if (cache != null) {
            cache.evictIfPresent(race.getId().longValue());
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
