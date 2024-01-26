package org.tiatus.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tiatus.entity.EntryPositionTime;
import org.tiatus.entity.Position;
import org.tiatus.entity.Race;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnreynolds on 19/06/2016.
 */
@Service
public class EntryPositionTimeDaoImpl implements EntryPositionTimeDao {

    private static final Logger LOG = LoggerFactory.getLogger(EntryPositionTimeDaoImpl.class);

    @Autowired
    EntryPositionTimeRepository repository;

    public List<EntryPositionTime> getPositionTimesForPositionInRace(Race race, Position position) throws DaoException {
        if (race != null && position != null) {
            return repository.getPositionTimesForPositionInRace(position.getId(), race.getId());

        } else {
            LOG.warn("Got null for race or position");
            return new ArrayList<>();
        }
    }

    public List<EntryPositionTime> getAllTimesForRace(Race race) throws DaoException {
        if (race != null) {
            return repository.getAllTimesForRace(race.getId());

        } else {
            LOG.warn("Got null for race");
            return new ArrayList<>();
        }
    }

    @Override
    public List<EntryPositionTime> getTimesForRace(Race race) throws DaoException {
        if (race != null) {
            return repository.getTimesForRace(race.getId());

        } else {
            LOG.warn("Got null for race");
            return new ArrayList<>();
        }
    }

    @Override
    public void createTime(EntryPositionTime entryPositionTime) throws DaoException {
        try {
            if (entryPositionTime.getPosition() != null && entryPositionTime.getEntry() != null) {
                if (repository.getTimesForEntryAtPosition(entryPositionTime.getEntry().getId(), entryPositionTime.getPosition().getId()).isEmpty()) {
                    entryPositionTime.setSynced(true);
                    repository.save(entryPositionTime);

                } else {
                    String message = "Failed to create time due to existing entryPositionTime with same position " + entryPositionTime.getPosition().getId() + " and entry " + entryPositionTime.getEntry().getId();
                    LOG.warn(message);
                    throw new DaoException(message);
                }

            } else {
                String message = "Failed to create time due to entryPositionTime missing position or entry ";
                LOG.warn(message);
                throw new DaoException(message);
            }

        } catch (Exception e) {
            LOG.warn("Failed to create time", e);
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public void updateTime(EntryPositionTime entryPositionTime) throws DaoException {
        try {
            if (entryPositionTime.getPosition() != null && entryPositionTime.getEntry() != null) {
                if (!repository.getTimesForEntryAtPosition(entryPositionTime.getEntry().getId(), entryPositionTime.getPosition().getId()).isEmpty()) {
                    entryPositionTime.setSynced(true);
                    repository.save(entryPositionTime);

                } else {
                    String message = "Failed to update time as one does not exist for the position " + entryPositionTime.getPosition().getId() + " and entry " + entryPositionTime.getEntry().getId();
                    LOG.warn(message);
                    throw new DaoException(message);
                }

            } else {
                String message = "Failed to update time due to entryPositionTime missing position or entry ";
                LOG.warn(message);
                throw new DaoException(message);
            }

        } catch (Exception e) {
            LOG.warn("Failed to update time", e);
            throw new DaoException(e.getMessage());
        }
    }

}
