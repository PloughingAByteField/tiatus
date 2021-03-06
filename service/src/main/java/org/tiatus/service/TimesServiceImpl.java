package org.tiatus.service;

import org.tiatus.dao.DaoException;
import org.tiatus.dao.EntryPositionTimeDao;
import org.tiatus.entity.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.jms.JMSException;
import java.sql.Timestamp;
import java.util.*;

@Default
public class TimesServiceImpl implements TimesService {

    private static final Logger LOG = LoggerFactory.getLogger(TimesServiceImpl.class);

    private final EntryPositionTimeDao dao;
    private MessageSenderService sender;

    @Inject
    public TimesServiceImpl(EntryPositionTimeDao dao, MessageSenderService sender) {
        this.dao = dao;
        this.sender = sender;
    }

    @Override
    public void createTime(EntryPositionTime entryPositionTime, String sessionId) throws ServiceException {
        try {
            dao.createTime(entryPositionTime);
            Message message = Message.createMessage(entryPositionTime, MessageType.ADD, sessionId);
            sender.sendMessage(message);

        } catch (DaoException e) {
            LOG.warn("Got dao exception");
            throw new ServiceException(e);
        } catch (JMSException e) {
            LOG.warn("Got jms exception", e);
            throw new ServiceException(e);
        }
    }

    @Override
    public void updateTime(EntryPositionTime entryPositionTime, String sessionId) throws ServiceException {
        try {
            dao.updateTime(entryPositionTime);
            Message message = Message.createMessage(entryPositionTime, MessageType.UPDATE, sessionId);
            sender.sendMessage(message);

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
        LOG.debug("Get list of times for race " + race.getName() + " at position " + position.getName());
        try {
            return dao.getPositionTimesForPositionInRace(race, position);

        } catch (DaoException e) {
            LOG.warn("Got dao exception");
            throw new ServiceException(e);
        }
    }

    @Override
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
    public List<EntryPositionTime> getTimesForRace(Race race) throws ServiceException {
        LOG.debug("Get full list of times for race " + race.getName());
        try {
            return dao.getTimesForRace(race);

        } catch (DaoException e) {
            LOG.warn("Got dao exception");
            throw new ServiceException(e);
        }
    }
}
