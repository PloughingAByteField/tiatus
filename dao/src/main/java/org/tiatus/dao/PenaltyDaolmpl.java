package org.tiatus.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.entity.Penalty;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.*;
import java.util.List;

/**
 * Created by johnreynolds on 01/03/2017.
 */
public class PenaltyDaolmpl implements PenaltyDao {
    private static final Logger LOG = LoggerFactory.getLogger(PenaltyDaolmpl.class);

    @PersistenceContext(unitName = "primary")
    protected EntityManager em;

    @Resource
    protected UserTransaction tx;

    @Override
    public List<Penalty> getPenalties() {
        return em.createQuery("FROM Penalty", Penalty.class).getResultList();
    }

    @Override
    public Penalty addPenalty(Penalty penalty) throws DaoException {
        LOG.debug("Adding penalty for entry " + penalty.getEntry().getId());
        try {
            Penalty existing = null;
            if (penalty.getId() != null) {
                existing = em.find(Penalty.class, penalty.getId());
            }
            if (existing == null) {
                tx.begin();
                Penalty merged = em.merge(penalty);
                tx.commit();

                return merged;
            } else {
                String message = "Failed to add penalty due to existing penalty with same id " + penalty.getId();
                LOG.warn(message);
                throw new DaoException(message);
            }
        } catch (NotSupportedException | SystemException | HeuristicMixedException | HeuristicRollbackException | RollbackException e) {
            LOG.warn("Failed to persist penalty", e.getMessage());
            throw new DaoException(e);
        }
    }

    @Override
    public void removePenalty(Penalty penalty) throws DaoException {
        try {
            Penalty existing = null;
            if (penalty.getId() != null) {
                existing = em.find(Penalty.class, penalty.getId());
            }
            if (existing != null) {
                tx.begin();
                em.remove(em.contains(penalty) ? penalty : em.merge(penalty));
                tx.commit();
            } else {
                LOG.warn("No such penalty of id " + penalty.getId());
            }
        } catch (NotSupportedException | SystemException | HeuristicMixedException | HeuristicRollbackException | RollbackException e) {
            LOG.warn("Failed to delete penalty", e);
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public void updatePenalty(Penalty penalty) throws DaoException {
        try {
            tx.begin();
            em.merge(penalty);
            tx.commit();
        } catch (NotSupportedException | SystemException | HeuristicMixedException | HeuristicRollbackException | RollbackException e) {
            LOG.warn("Failed to update penalty", e);
            throw new DaoException(e.getMessage());
        }
    }
}
