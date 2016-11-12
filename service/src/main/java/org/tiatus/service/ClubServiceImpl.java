package org.tiatus.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.dao.DaoException;
import org.tiatus.dao.ClubDao;
import org.tiatus.entity.Club;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.util.List;

/**
 * Created by johnreynolds on 25/06/2016.
 */
@Default
public class ClubServiceImpl implements ClubService {

    private static final Logger LOG = LoggerFactory.getLogger(ClubServiceImpl.class);

    private final ClubDao dao;

    /**
     * Constructor for service
     * @param dao object injected by cdi
     */
    @Inject
    public ClubServiceImpl(ClubDao dao) {
       this.dao = dao;
    }

    @Override
    public Club addClub(Club club) throws ServiceException {
        LOG.debug("Adding club " + club);
        try {
            return dao.addClub(club);

        } catch (DaoException e) {
            LOG.warn("Got dao exception");
            throw new ServiceException(e);
        }
    }

    @Override
    public void deleteClub(Club club) throws ServiceException {
        LOG.debug("Delete club " + club.getId());
        try {
            dao.removeClub(club);
        } catch (DaoException e) {
            LOG.warn("Got dao exception");
            throw new ServiceException(e);
        }
    }

    @Override
    public void updateClub(Club club) throws ServiceException {
        LOG.debug("Update club " + club.getId());
        try {
            dao.updateClub(club);
        } catch (DaoException e) {
            LOG.warn("Got dao exception");
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Club> getClubs() {
        return dao.getClubs();
    }
}
