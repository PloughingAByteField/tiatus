package org.tiatus.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tiatus.entity.Race;

import java.util.List;

/**
 * Created by johnreynolds on 19/06/2016.
 */
@Service
public class RaceDaoImpl implements RaceDao {

    private static final Logger LOG = LoggerFactory.getLogger(RaceDaoImpl.class);

    @Autowired
    private RaceRepository repository;

    @Override
    public List<Race> getRaces() {
        return repository.findByOrderByRaceOrder();
    }

    @Override
    public Race getRaceForId(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Race addRace(Race race) throws DaoException {
        LOG.debug("Adding race " + race);
        try {
            if (!repository.existsById(race.getId())) {
                return repository.save(race);

            } else {
                String message = "Failed to add race due to existing race with same id " + race.getId();
                LOG.warn(message);
                throw new DaoException(message);
            }

        } catch (Exception e) {
            LOG.warn("Failed to persist race", e);
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public void removeRace(Race race) throws DaoException {
        try {
            if (repository.existsById(race.getId())) {
                repository.delete(race);

            } else {
                LOG.warn("No such race of id " + race.getId());
            }

        } catch (Exception e) {
            LOG.warn("Failed to delete race", e);
            throw new DaoException(e.getMessage());
        }

    }

    @Override
    public void updateRace(Race race) throws DaoException {
        try {
            repository.save(race);

        } catch (Exception e) {
            LOG.warn("Failed to update race", e);
            throw new DaoException(e.getMessage());
        }
    }
}
