package org.tiatus.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tiatus.entity.Disqualification;

import java.util.List;

/**
 * Created by johnreynolds on 01/03/2017.
 */
@Service
public class DisqualificationDaoImpl implements DisqualificationDao {
    private static final Logger LOG = LoggerFactory.getLogger(DisqualificationDaoImpl.class);

    @Autowired
    DisqualificationRepository repository;

    @Override
    public List<Disqualification> getDisqualifications() {
        return repository.findAll();
    }

    @Override
    public Disqualification addDisqualification(Disqualification disqualification) throws DaoException {
        LOG.debug("Adding disqualification for entry " + disqualification.getEntry().getId());
        try {
            boolean exists = false;
            if (disqualification.getId() != null) {
                exists = repository.existsById(disqualification.getId());
            }
            if (!exists) {
                return repository.save(disqualification);

            } else {
                String message = "Failed to add disqualification due to existing disqualification with same id " + disqualification.getId();
                LOG.warn(message);
                throw new DaoException(message);
            }

        } catch (Exception e) {
            LOG.warn("Failed to persist disqualification", e.getMessage());
            throw new DaoException(e);
        }
    }

    @Override
    public void removeDisqualification(Disqualification disqualification) throws DaoException {
        try {
            boolean exists = false;
            if (disqualification.getId() != null) {
                exists = repository.existsById(disqualification.getId());
            }
            if (exists) {
                repository.delete(disqualification);

            } else {
                LOG.warn("No such disqualification of id " + disqualification.getId());
            }
        } catch (Exception e) {
            LOG.warn("Failed to delete disqualification", e.getMessage());
            throw new DaoException(e);
        }
    }

    @Override
    public Disqualification updateDisqualification(Disqualification disqualification) throws DaoException {
        try {
            return repository.save(disqualification);

        } catch (Exception e) {
            LOG.warn("Failed to update disqualification", e.getMessage());
            throw new DaoException(e);
        }
    }

    @Override
    public Disqualification getDisqualificationForId(Long id) {
        return repository.findById(id).orElse(null);
    }
}
