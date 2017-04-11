package org.tiatus.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.entity.Race;

import javax.annotation.Resource;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.*;
import java.math.BigInteger;
import java.util.List;



/**
 * Created by johnreynolds on 19/06/2016.
 */
@Default
public class RaceDaoImpl implements RaceDao {

    private static final Logger LOG = LoggerFactory.getLogger(RaceDaoImpl.class);

    @PersistenceContext(unitName = "primary")
    protected EntityManager em;

    @Resource
    protected UserTransaction tx;

    @Override
    public List<Race> getRaces() {
        TypedQuery<Race> query = em.createQuery("FROM Race order by raceOrder", Race.class);
        return query.getResultList();
    }

    @Override
    public Race getRaceForId(Long id) {
        return em.find(Race.class, id);
    }

    @Override
    public Race addRace(Race race) throws DaoException {
        LOG.debug("Adding race " + race);
        try {
            tx.begin();
            Race existing = null;
            if (race.getId() != null) {
                existing = em.find(Race.class, race.getId());
            }
            if (existing == null) {
                Race merged = em.merge(race);
                tx.commit();

                return merged;
            } else {
                String message = "Failed to add race due to existing race with same id " + race.getId();
                LOG.warn(message);
                tx.rollback();
                throw new DaoException(message);
            }
        } catch (DaoException e) {
            throw e;
        } catch (Exception e) {
            LOG.warn("Failed to persist race", e);
            try { tx.rollback(); } catch (Exception se) { LOG.warn("Failed to rollback", se); }
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public void removeRace(Race race) throws DaoException {
        try {
            tx.begin();
            Race existing = null;
            if (race.getId() != null) {
                existing = em.find(Race.class, race.getId());
            }
            if (existing != null) {
                em.remove(em.contains(race) ? race : em.merge(race));
                tx.commit();
            } else {
                LOG.warn("No such race of id " + race.getId());
                tx.rollback();
            }
        } catch (Exception e) {
            LOG.warn("Failed to delete race", e);
            try { tx.rollback(); } catch (Exception se) { LOG.warn("Failed to rollback", se); }
            throw new DaoException(e.getMessage());
        }

    }

    @Override
    public void updateRace(Race race) throws DaoException {
        try {
            tx.begin();
            em.merge(race);
            tx.commit();
        } catch (Exception e) {
            LOG.warn("Failed to update race", e);
            try { tx.rollback(); } catch (Exception se) { LOG.warn("Failed to rollback", se); }
            throw new DaoException(e.getMessage());
        }
    }
}
