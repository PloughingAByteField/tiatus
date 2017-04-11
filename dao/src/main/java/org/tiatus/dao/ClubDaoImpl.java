package org.tiatus.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.entity.Club;

import javax.annotation.Resource;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.*;
import java.util.List;


/**
 * Created by johnreynolds on 19/06/2016.
 */
@Default
public class ClubDaoImpl implements ClubDao {

    private static final Logger LOG = LoggerFactory.getLogger(ClubDaoImpl.class);

    @PersistenceContext(unitName = "primary")
    protected EntityManager em;

    @Resource
    protected UserTransaction tx;

    @Override
    public List<Club> getClubs() {
        TypedQuery<Club> query = em.createQuery("FROM Club", Club.class);
        return query.getResultList();
    }

    @Override
    public Club addClub(Club club) throws DaoException {
        LOG.debug("Adding club " + club);
        try {
            tx.begin();
            Club existing = null;
            if (club.getId() != null) {
                existing = em.find(Club.class, club.getId());
            }
            if (existing == null) {
                Club merged = em.merge(club);
                tx.commit();

                return merged;
            } else {
                String message = "Failed to add club due to existing club with same id " + club.getId();
                LOG.warn(message);
                tx.rollback();
                throw new DaoException(message);
            }
        } catch (DaoException e) {
            throw e;
        } catch (Exception e) {
            LOG.warn("Failed to persist club", e.getMessage());
            try { tx.rollback(); } catch (SystemException se) { LOG.warn("Failed to rollback", se); }
            throw new DaoException(e);
        }
    }

    @Override
    public void removeClub(Club club) throws DaoException {
        try {
            tx.begin();
            Club existing = null;
            if (club.getId() != null) {
                existing = em.find(Club.class, club.getId());
            }
            if (existing != null) {
                em.remove(em.contains(club) ? club : em.merge(club));
                tx.commit();
            } else {
                LOG.warn("No such club of id " + club.getId());
                tx.rollback();
            }
        } catch (Exception e) {
            LOG.warn("Failed to delete club", e.getMessage());
            try { tx.rollback(); } catch (SystemException se) { LOG.warn("Failed to rollback", se); }
            throw new DaoException(e);
        }

    }

    @Override
    public void updateClub(Club club) throws DaoException {
        try {
            tx.begin();
            em.merge(club);
            tx.commit();
        } catch (Exception e) {
            LOG.warn("Failed to update club", e.getMessage());
            try { tx.rollback(); } catch (SystemException se) { LOG.warn("Failed to rollback", se); }
            throw new DaoException(e);
        }
    }

    @Override
    public Club getClubForId(Long id) {
        return em.find(Club.class, id);
    }
}
