package org.tiatus.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.entity.Position;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.*;
import java.util.List;

/**
 * Created by johnreynolds on 11/11/2016.
 */
public class PositionDaoImpl implements PositionDao {

    private static final Logger LOG = LoggerFactory.getLogger(PositionDaoImpl.class);

    @PersistenceContext(unitName = "primary")
    protected EntityManager em;

    @Resource
    protected UserTransaction tx;

    @Override
    public List<Position> getPositions() {
        TypedQuery<Position> query = em.createQuery("FROM Position order by order", Position.class);
        return query.getResultList();
    }

    @Override
    public List<Position> getActiveTimingPositions() {
        TypedQuery<Position> query = em.createQuery("FROM Position where timing = 't' and active = 't' order by order", Position.class);
        return query.getResultList();
    }

    @Override
    public Position addPosition(Position position) throws DaoException {
        LOG.debug("Adding position " + position);
        try {
            Position existing = null;
            if (position.getId() != null) {
                existing = em.find(Position.class, position.getId());
            }
            if (existing == null) {
                tx.begin();
                Position merged = em.merge(position);
                tx.commit();

                return merged;
            } else {
                String message = "Failed to add position due to existing position with same id " + position.getId();
                LOG.warn(message);
                throw new DaoException(message);
            }
        } catch (NotSupportedException | SystemException | HeuristicMixedException | HeuristicRollbackException | RollbackException e) {
            LOG.warn("Failed to persist position", e.getMessage());
            throw new DaoException(e);
        }
    }

    @Override
    public void removePosition(Position position) throws DaoException {
        try {
            Position existing = null;
            if (position.getId() != null) {
                existing = em.find(Position.class, position.getId());
            }
            if (existing != null) {
                tx.begin();
                em.remove(em.contains(position) ? position : em.merge(position));
                tx.commit();
            } else {
                LOG.warn("No such position of id " + position.getId());
            }
        } catch (NotSupportedException | SystemException | HeuristicMixedException | HeuristicRollbackException | RollbackException e) {
            LOG.warn("Failed to delete position", e);
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public void updatePosition(Position position) throws DaoException {
        try {
            tx.begin();
            em.merge(position);
            tx.commit();
        } catch (NotSupportedException | SystemException | HeuristicMixedException | HeuristicRollbackException | RollbackException e) {
            LOG.warn("Failed to update position", e);
            throw new DaoException(e.getMessage());
        }
    }
}
