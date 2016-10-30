package org.tiatus.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.entity.RaceEvent;

import javax.annotation.Resource;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.*;
import java.util.List;

/**
 * Created by johnreynolds on 10/10/2016.
 */
@Default
public class RaceEventDaoImpl implements RaceEventDao {

    private static final Logger LOG = LoggerFactory.getLogger(RaceEventDaoImpl.class);

    @PersistenceContext(unitName = "primary")
    protected EntityManager em;

    @Resource
    protected UserTransaction tx;

    @Override
    public RaceEvent addRaceEvent(RaceEvent raceEvent) throws DaoException {
        LOG.debug("Adding raceEvent " + raceEvent);
        try {
            RaceEvent existing = null;
            if (raceEvent.getId() != null) {
                existing = em.find(RaceEvent.class, raceEvent.getId());
            }
            if (existing == null) {
                tx.begin();
                RaceEvent merged = em.merge(raceEvent);
                tx.commit();

                return merged;
            } else {
                String message = "Failed to add raceEvent due to existing raceEvent with same id " + raceEvent.getId();
                LOG.warn(message);
                throw new DaoException(message);
            }
        } catch (NotSupportedException | SystemException | HeuristicMixedException | HeuristicRollbackException | RollbackException e) {
            LOG.warn("Failed to persist event", e.getMessage());
            throw new DaoException(e);
        }
    }

    @Override
    public void deleteRaceEvent(RaceEvent raceEvent) throws DaoException {
        try {
            RaceEvent existing = null;
            if (raceEvent.getId() != null) {
                existing = em.find(RaceEvent.class, raceEvent.getId());
            }
            if (existing != null) {
                tx.begin();
                em.remove(em.contains(raceEvent) ? raceEvent : em.merge(raceEvent));
                tx.commit();
            } else {
                LOG.warn("No such event of id " + raceEvent.getId());
            }
        } catch (NotSupportedException | SystemException | HeuristicMixedException | HeuristicRollbackException | RollbackException e) {
            LOG.warn("Failed to delete raceEvent", e);
            throw new DaoException(e.getMessage());
        }
    }


    @Override
    public void updateRaceEvent(RaceEvent raceEvent) throws DaoException {
        try {
            tx.begin();
            em.merge(raceEvent);
            tx.commit();
        } catch (NotSupportedException | SystemException | HeuristicMixedException | HeuristicRollbackException | RollbackException e) {
            LOG.warn("Failed to update raceEvent", e);
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public List<RaceEvent> getRaceEvents() {
        TypedQuery<RaceEvent> query = em.createQuery("FROM RaceEvent order by raceEventOrder", RaceEvent.class);
        return query.getResultList();
    }

}
