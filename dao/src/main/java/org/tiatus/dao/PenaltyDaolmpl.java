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
            tx.begin();
            Penalty existing = null;
            if (penalty.getId() != null) {
                existing = em.find(Penalty.class, penalty.getId());
            }
            if (existing == null) {
                em.persist(penalty);
                tx.commit();
                return penalty;

            } else {
                String message = "Failed to add penalty due to existing penalty with same id " + penalty.getId();
                LOG.warn(message);
                tx.rollback();
                throw new DaoException(message);
            }
        } catch (DaoException e) {
            throw e;
        } catch (Exception e) {
            LOG.warn("Failed to persist penalty", e);
            try { tx.rollback(); } catch (Exception se) { LOG.warn("Failed to rollback", se); }
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public void removePenalty(Penalty penalty) throws DaoException {
        try {
            tx.begin();
            Penalty existing = null;
            if (penalty.getId() != null) {
                existing = em.find(Penalty.class, penalty.getId());
            }
            if (existing != null) {
                em.remove(em.contains(penalty) ? penalty : em.merge(penalty));
                tx.commit();
            } else {
                LOG.warn("No such penalty of id " + penalty.getId());
                tx.rollback();
            }
        } catch (Exception e) {
            LOG.warn("Failed to delete penalty", e);
            try { tx.rollback(); } catch (Exception se) { LOG.warn("Failed to rollback", se); }
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public void updatePenalty(Penalty penalty) throws DaoException {
        try {
            tx.begin();
            em.merge(penalty);
            tx.commit();
        } catch (Exception e) {
            LOG.warn("Failed to update penalty", e);
            try { tx.rollback(); } catch (Exception se) { LOG.warn("Failed to rollback", se); }
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public Penalty getPenaltyForId(Long id) {
        return em.find(Penalty.class, id);
    }
}
