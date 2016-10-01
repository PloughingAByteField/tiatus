package org.tiatus.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.dao.DaoException;
import org.tiatus.dao.RaceDao;
import org.tiatus.entity.Race;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.util.List;

/**
 * Created by johnreynolds on 25/06/2016.
 */
@Default
public class RaceServiceImpl implements RaceService {

    private static final Logger LOG = LoggerFactory.getLogger(RaceServiceImpl.class);

    private final RaceDao dao;

    /**
     * Constructor for service
     * @param dao object injected by cdi
     */
    @Inject
    public RaceServiceImpl(RaceDao dao) {
       this.dao = dao;
    }

    @Override
    public Race addRace(Race race) throws ServiceException {
        LOG.debug("Adding race " + race);
        try {
            return dao.addRace(race);

        } catch (DaoException e) {
            LOG.warn("Got dao exception");
            throw new ServiceException(e);
        }
    }

    @Override
    public void deleteRace(Race race) throws ServiceException {
        LOG.debug("Delete race " + race.getId());
        try {
            dao.removeRace(race);
        } catch (DaoException e) {
            LOG.warn("Got dao exception");
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Race> getRaces() {
        return dao.getRaces();
    }
}
