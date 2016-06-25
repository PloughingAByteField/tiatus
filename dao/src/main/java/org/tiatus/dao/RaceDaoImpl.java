package org.tiatus.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.entity.Race;

import javax.annotation.Resource;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
        Query query = em.createQuery("FROM Race order by raceOrder");
        return query.getResultList();
    }

    @Override
    public void addRace(Race race) {
        LOG.debug("Adding race " + race);
        try {
            tx.begin();
            em.merge(race);
            tx.commit();
        } catch (NotSupportedException | SystemException | HeuristicMixedException | HeuristicRollbackException | RollbackException e) {
            LOG.warn("Failed to persist race", e);
        }
    }

    @Override
    public void removeRace(Race race) {
        try {
            tx.begin();
            em.remove(em.contains(race) ? race : em.merge(race));
            tx.commit();
        } catch (NotSupportedException | SystemException | HeuristicMixedException | HeuristicRollbackException | RollbackException e) {
            LOG.warn("Failed to delete race", e);
        }

    }

    @Override
    public void updateRace(Race race) {
        try {
            tx.begin();
            em.merge(race);
            tx.commit();
        } catch (NotSupportedException | SystemException | HeuristicMixedException | HeuristicRollbackException | RollbackException e) {
            LOG.warn("Failed to update race", e);
        }
    }
}
