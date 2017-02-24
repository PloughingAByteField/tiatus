package org.tiatus.service;

import org.tiatus.dao.DaoException;
import org.tiatus.dao.EntryPositionTimeDao;
import org.tiatus.entity.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.sql.Timestamp;
import java.util.*;

@Default
public class TimesServiceImpl implements TimesService {

    private static final Logger LOG = LoggerFactory.getLogger(TimesServiceImpl.class);

    private final EntryPositionTimeDao dao;

    @Inject
    public TimesServiceImpl(EntryPositionTimeDao dao) {
        this.dao = dao;
    }

    @Override
    public void createTime(EntryPositionTime entryPositionTime) throws ServiceException {
        try {
            dao.createTime(entryPositionTime);
        } catch (DaoException e) {
            LOG.warn("Got dao exception");
            throw new ServiceException(e);
        }
    }

    @Override
    public void updateTime(EntryPositionTime entryPositionTime) throws ServiceException {
        try {
            dao.updateTime(entryPositionTime);
        } catch (DaoException e) {
            LOG.warn("Got dao exception");
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
    public List<EntryPositionTime> getTimesForRace(Race race) throws ServiceException {
        LOG.debug("Get list of times for race " + race.getName());
        try {
            return dao.getTimesForRace(race);

        } catch (DaoException e) {
            LOG.warn("Got dao exception");
            throw new ServiceException(e);
        }
    }


}
