package org.tiatus.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.dao.DaoException;
import org.tiatus.dao.DisqualificationDao;
import org.tiatus.entity.Disqualification;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by johnreynolds on 01/03/2017.
 */
public class DisqualificationServiceImpl implements DisqualificationService {
    private static final Logger LOG = LoggerFactory.getLogger(DisqualificationServiceImpl.class);

    private final DisqualificationDao dao;

    /**
     * Constructor for service
     * @param dao object injected by cdi
     */
    @Inject
    public DisqualificationServiceImpl(DisqualificationDao dao) {
        this.dao = dao;
    }

    @Override
    public Disqualification addDisqualification(Disqualification disqualification) throws ServiceException {
        LOG.debug("Adding disqualification for entry " + disqualification.getEntry().getId());
        try {
            return dao.addDisqualification(disqualification);

        } catch (DaoException e) {
            LOG.warn("Got dao exception");
            throw new ServiceException(e);
        }
    }

    @Override
    public void deleteDisqualification(Disqualification disqualification) throws ServiceException {
        LOG.debug("Delete disqualification " + disqualification.getId());
        try {
            dao.removeDisqualification(disqualification);
        } catch (DaoException e) {
            LOG.warn("Got dao exception");
            throw new ServiceException(e);
        }
    }

    @Override
    public void updateDisqualification(Disqualification disqualification) throws ServiceException {
        LOG.debug("Update disqualification " + disqualification.getId());
        try {
            dao.updateDisqualification(disqualification);
        } catch (DaoException e) {
            LOG.warn("Got dao exception");
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Disqualification> getDisqualifications() {
        return dao.getDisqualifications();
    }
}
