package org.tiatus.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.dao.DaoException;
import org.tiatus.dao.PenaltyDao;
import org.tiatus.entity.Penalty;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by johnreynolds on 01/03/2017.
 */
public class PenaltyServiceImpl implements PenaltyService {
    private static final Logger LOG = LoggerFactory.getLogger(PenaltyServiceImpl.class);

    private final PenaltyDao dao;

    /**
     * Constructor for service
     * @param dao object injected by cdi
     */
    @Inject
    public PenaltyServiceImpl(PenaltyDao dao) {
        this.dao = dao;
    }

    @Override
    public Penalty addPenalty(Penalty penalty) throws ServiceException {
        LOG.debug("Adding penalty for entry " + penalty.getEntry().getId());
        try {
            return dao.addPenalty(penalty);

        } catch (DaoException e) {
            LOG.warn("Got dao exception");
            throw new ServiceException(e);
        }
    }

    @Override
    public void deletePenalty(Penalty penalty) throws ServiceException {
        LOG.debug("Delete penalty " + penalty.getId());
        try {
            dao.removePenalty(penalty);
        } catch (DaoException e) {
            LOG.warn("Got dao exception");
            throw new ServiceException(e);
        }
    }

    @Override
    public void updatePenalty(Penalty penalty) throws ServiceException {
        LOG.debug("Update penalty " + penalty.getId());
        try {
            dao.updatePenalty(penalty);
        } catch (DaoException e) {
            LOG.warn("Got dao exception");
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Penalty> getPenalties() {
        return dao.getPenalties();
    }
}
