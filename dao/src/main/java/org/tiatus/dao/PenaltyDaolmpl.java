package org.tiatus.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tiatus.entity.Penalty;

import java.util.List;

/**
 * Created by johnreynolds on 01/03/2017.
 */
@Service
public class PenaltyDaolmpl implements PenaltyDao {
    private static final Logger LOG = LoggerFactory.getLogger(PenaltyDaolmpl.class);

    @Autowired
    private PenaltyRepository repository;

    @Override
    public List<Penalty> getPenalties() {
        return repository.findAll();
    }

    @Override
    public Penalty addPenalty(Penalty penalty) throws DaoException {
        LOG.debug("Adding penalty for entry " + penalty.getEntry().getId());
        try {
            Penalty existing = null;
            if (penalty.getId() != null) {
                existing = repository.findById(penalty.getId()).orElse(null);
            }
            if (existing == null) {
                return repository.save(penalty);

            } else {
                String message = "Failed to add penalty due to existing penalty with same id " + penalty.getId();
                LOG.warn(message);
                throw new DaoException(message);
            }

        } catch (Exception e) {
            LOG.warn("Failed to persist penalty", e);
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public void removePenalty(Penalty penalty) throws DaoException {
        try {
            if (repository.existsById(penalty.getId())) {
                repository.delete(penalty);

            } else {
                LOG.warn("No such penalty of id " + penalty.getId());
            }

        } catch (Exception e) {
            LOG.warn("Failed to delete penalty", e);
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public Penalty updatePenalty(Penalty penalty) throws DaoException {
        try {
            return repository.save(penalty);

        } catch (Exception e) {
            LOG.warn("Failed to update penalty", e);
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public Penalty getPenaltyForId(Long id) {
        return repository.findById(id).orElse(null);
    }
}
