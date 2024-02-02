package org.tiatus.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tiatus.entity.Club;

import java.util.List;

/**
 * Created by johnreynolds on 19/06/2016.
 */
@Service
public class ClubDaoImpl implements ClubDao {

    private static final Logger LOG = LoggerFactory.getLogger(ClubDaoImpl.class);

    @Autowired
    protected ClubRepository repository;
    
    @Override
    public List<Club> getClubs() {
        return repository.findAll();
    }

    @Override
    public Club addClub(Club club) throws DaoException {
        LOG.debug("Adding club " + club);

        try {
            boolean exists = false;
            if (club.getId() != null) {
                exists = repository.existsById(club.getId());
            }
            if (!exists) {
                return repository.save(club);

            } else {
                String message = "Failed to add club due to existing club with same id " + club.getId();
                LOG.warn(message);
                throw new DaoException(message);
            }

        } catch (Exception e) {
            LOG.warn("Failed to persist club", e.getMessage());
            throw new DaoException(e);
        }
    }

    @Override
    public void removeClub(Club club) throws DaoException {
        try {
            boolean exists = false;
            if (club.getId() != null) {
                exists = repository.existsById(club.getId());
            }
            if (exists) {
                repository.delete(club);

            } else {
                String message = "No such club of id " + club.getId();
                LOG.warn(message);
                throw new DaoException(message);
            }
            
        } catch (Exception e) {
            LOG.warn("Failed to delete club", e.getMessage());
            throw new DaoException(e);
        }
    }

    @Override
    public Club updateClub(Club club) throws DaoException {
        try {
            Club existing = null;
            if (club.getId() != null) {
                existing = repository.findById(club.getId()).orElse(null);
            }
            if (existing != null) {
                return repository.save(club);

            } else {
                String message = "No such club of id " + club.getId();
                LOG.warn(message);
                throw new DaoException(message);
            }

        } catch (Exception e) {
            LOG.warn("Failed to update club", e.getMessage());
            throw new DaoException(e);
        }
    }

    @Override
    public Club getClubForId(Long id) {
        return repository.findById(id).orElse(null);
    }
}
