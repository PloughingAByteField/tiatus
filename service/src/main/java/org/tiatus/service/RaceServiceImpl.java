package org.tiatus.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.dao.DaoException;
import org.tiatus.dao.RaceDao;
import org.tiatus.entity.Race;

import javax.enterprise.inject.Default;
import javax.inject.Inject;

/**
 * Created by johnreynolds on 25/06/2016.
 */
@Default
public class RaceServiceImpl implements RaceService {

    private static final Logger LOG = LoggerFactory.getLogger(RaceService.class);

    @Inject
    private RaceDao dao;

    @Override
    public Race addRace(Race race) throws ServiceException {
        LOG.debug("Adding race " + race);
        try {
            return dao.addRace(race);

        } catch (DaoException e) {
            LOG.warn("Got dao exception " + e);
            throw new ServiceException(e.getMessage());
        }
    }
}
