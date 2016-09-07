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
    public Race addRace(Race race) throws DaoException {
        LOG.debug("Adding race " + race);
        try {
            Race existing = null;
            if (race.getId() != null) {
                existing = em.find(Race.class, race.getId());
            }
            if (existing == null) {
                tx.begin();
                Race merged = em.merge(race);
                tx.commit();

                return merged;
            } else {
                String message = "Failed to add race due to existing race with same id " + race.getId();
                LOG.warn(message);
                throw new DaoException(message);
            }
        } catch (NotSupportedException | SystemException | HeuristicMixedException | HeuristicRollbackException | RollbackException e) {
            LOG.warn("Failed to persist race", e.getMessage());
            throw new DaoException(e);
        }
    }

    @Override
    public void removeRace(Race race) throws DaoException {
        try {
            tx.begin();
            em.remove(em.contains(race) ? race : em.merge(race));
            tx.commit();
        } catch (NotSupportedException | SystemException | HeuristicMixedException | HeuristicRollbackException | RollbackException e) {
            LOG.warn("Failed to delete race", e);
            throw new DaoException(e.getMessage());
        }

    }

    @Override
    public void updateRace(Race race) throws DaoException {
        try {
            tx.begin();
            em.merge(race);
            tx.commit();
        } catch (NotSupportedException | SystemException | HeuristicMixedException | HeuristicRollbackException | RollbackException e) {
            LOG.warn("Failed to update race", e);
            throw new DaoException(e.getMessage());
        }
    }
}
