package org.tiatus.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.dao.DaoException;
import org.tiatus.dao.PositionDao;
import org.tiatus.entity.Position;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.util.List;

/**
 * Created by johnreynolds on 25/06/2016.
 */
@Default
public class PositionServiceImpl implements PositionService {

    private static final Logger LOG = LoggerFactory.getLogger(PositionServiceImpl.class);

    private final PositionDao dao;

    /**
     * Constructor for service
     * @param dao object injected by cdi
     */
    @Inject
    public PositionServiceImpl(PositionDao dao) {
       this.dao = dao;
    }


    @Override
    public Position addPosition(Position position) throws ServiceException {
        LOG.debug("Adding position " + position);
        try {
            return dao.addPosition(position);

        } catch (DaoException e) {
            LOG.warn("Got dao exception");
            throw new ServiceException(e);
        }
    }

    @Override
    public void removePosition(Position position) throws ServiceException {
        LOG.debug("Delete position " + position.getId());
        try {
            dao.removePosition(position);
        } catch (DaoException e) {
            LOG.warn("Got dao exception");
            throw new ServiceException(e);
        }
    }

    @Override
    public Position getPositionForId(Long id) {
        return dao.getPositionForId(id);
    }

    @Override
    public List<Position> getPositions() {
        return dao.getPositions();
    }

    @Override
    public List<Position> getActiveTimingPositions() {
        return dao.getActiveTimingPositions();
    }

    @Override
    public void updatePosition(Position position) throws ServiceException {
        try {
            dao.updatePosition(position);
        } catch (DaoException e) {
            LOG.warn("Got dao exception");
            throw new ServiceException(e);
        }
    }
}
