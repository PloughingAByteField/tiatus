package org.tiatus.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.entity.Disqualification;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.*;
import java.util.List;

/**
 * Created by johnreynolds on 01/03/2017.
 */
public class DisqualificationDaoImpl implements DisqualificationDao {
    private static final Logger LOG = LoggerFactory.getLogger(DisqualificationDaoImpl.class);

    @PersistenceContext(unitName = "primary")
    protected EntityManager em;

    @Resource
    protected UserTransaction tx;

    @Override
    public List<Disqualification> getDisqualifications() {
        return em.createQuery("FROM Disqualification", Disqualification.class).getResultList();
    }

    @Override
    public Disqualification addDisqualification(Disqualification disqualification) throws DaoException {
        LOG.debug("Adding disqualification for entry " + disqualification.getEntry().getId());
        try {
            Disqualification existing = null;
            if (disqualification.getId() != null) {
                existing = em.find(Disqualification.class, disqualification.getId());
            }
            if (existing == null) {
                tx.begin();
                Disqualification merged = em.merge(disqualification);
                tx.commit();

                return merged;
            } else {
                String message = "Failed to add disqualification due to existing disqualification with same id " + disqualification.getId();
                LOG.warn(message);
                throw new DaoException(message);
            }
        } catch (NotSupportedException | SystemException | HeuristicMixedException | HeuristicRollbackException | RollbackException e) {
            LOG.warn("Failed to persist disqualification", e.getMessage());
            throw new DaoException(e);
        }
    }

    @Override
    public void removeDisqualification(Disqualification disqualification) throws DaoException {
        try {
            Disqualification existing = null;
            if (disqualification.getId() != null) {
                existing = em.find(Disqualification.class, disqualification.getId());
            }
            if (existing != null) {
                tx.begin();
                em.remove(em.contains(disqualification) ? disqualification : em.merge(disqualification));
                tx.commit();
            } else {
                LOG.warn("No such disqualification of id " + disqualification.getId());
            }
        } catch (NotSupportedException | SystemException | HeuristicMixedException | HeuristicRollbackException | RollbackException e) {
            LOG.warn("Failed to delete disqualification", e);
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public void updateDisqualification(Disqualification disqualification) throws DaoException {
        try {
            tx.begin();
            em.merge(disqualification);
            tx.commit();
        } catch (NotSupportedException | SystemException | HeuristicMixedException | HeuristicRollbackException | RollbackException e) {
            LOG.warn("Failed to update disqualification", e);
            throw new DaoException(e.getMessage());
        }
    }
}
