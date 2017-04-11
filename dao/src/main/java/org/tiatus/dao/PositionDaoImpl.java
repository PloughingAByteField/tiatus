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
    public Position getPositionForId(Long id) {
        return em.find(Position.class, id);
    }

    @Override
    public List<Position> getPositions() {
        TypedQuery<Position> query = em.createQuery("FROM Position order by name", Position.class);
        return query.getResultList();
    }

    @Override
    public Position addPosition(Position position) throws DaoException {
        LOG.debug("Adding position " + position);
        try {
            tx.begin();
            Position existing = null;
            if (position.getId() != null) {
                existing = em.find(Position.class, position.getId());
            }
            if (existing == null) {
                Position merged = em.merge(position);
                tx.commit();

                return merged;
            } else {
                String message = "Failed to add position due to existing position with same id " + position.getId();
                LOG.warn(message);
                tx.rollback();
                throw new DaoException(message);
            }
        } catch (DaoException e) {
            throw e;
        } catch (Exception e) {
            LOG.warn("Failed to persist position", e);
            try { tx.rollback(); } catch (Exception se) { LOG.warn("Failed to rollback", se); }
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public void removePosition(Position position) throws DaoException {
        try {
            tx.begin();
            Position existing = null;
            if (position.getId() != null) {
                existing = em.find(Position.class, position.getId());
            }
            if (existing != null) {
                em.remove(em.contains(position) ? position : em.merge(position));
                tx.commit();
            } else {
                LOG.warn("No such position of id " + position.getId());
                tx.rollback();
            }
        } catch (Exception e) {
            LOG.warn("Failed to delete position", e);
            try { tx.rollback(); } catch (Exception se) { LOG.warn("Failed to rollback", se); }
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public void updatePosition(Position position) throws DaoException {
        try {
            tx.begin();
            em.merge(position);
            tx.commit();
        } catch (Exception e) {
            LOG.warn("Failed to update position", e);
            try { tx.rollback(); } catch (Exception se) { LOG.warn("Failed to rollback", se); }
            throw new DaoException(e.getMessage());
        }
    }
}
