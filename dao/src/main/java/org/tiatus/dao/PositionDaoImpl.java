package org.tiatus.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tiatus.entity.Position;

import java.util.List;

/**
 * Created by johnreynolds on 11/11/2016.
 */
@Service
public class PositionDaoImpl implements PositionDao {

    private static final Logger LOG = LoggerFactory.getLogger(PositionDaoImpl.class);

    @Autowired
    private PositionRepository repository;

    @Override
    public Position getPositionForId(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<Position> getPositions() {
        return repository.findByOrderByName();
    }

    @Override
    public Position addPosition(Position position) throws DaoException {
        LOG.debug("Adding position " + position);
        try {
            if (!repository.existsById(position.getId())) {
                return repository.save(position);

            } else {
                String message = "Failed to add position due to existing position with same id " + position.getId();
                LOG.warn(message);
                throw new DaoException(message);
            }

        } catch (Exception e) {
            LOG.warn("Failed to persist position", e);
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public void removePosition(Position position) throws DaoException {
        try {
            if (repository.existsById(position.getId())) {
                repository.delete(position);

            } else {
                LOG.warn("No such position of id " + position.getId());
            }
        } catch (Exception e) {
            LOG.warn("Failed to delete position", e);
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public void updatePosition(Position position) throws DaoException {
        try {
            repository.save(position);

        } catch (Exception e) {
            LOG.warn("Failed to update position", e);
            throw new DaoException(e.getMessage());
        }
    }
}
