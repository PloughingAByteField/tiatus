package org.tiatus.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.entity.EntryPositionTime;
import org.tiatus.entity.Position;
import org.tiatus.entity.Race;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnreynolds on 19/06/2016.
 */
public class EntryPositionTimeDaoImpl implements EntryPositionTimeDao {

    private static final Logger LOG = LoggerFactory.getLogger(EntryPositionTimeDaoImpl.class);

    @PersistenceContext(unitName = "primary")
    protected EntityManager em;

    @Resource
    protected UserTransaction tx;

    public List<EntryPositionTime> getPositionTimesForPositionInRace(Race race, Position position) throws DaoException {
        if (race != null && position != null) {
            TypedQuery<EntryPositionTime> query = em.createQuery("FROM EntryPositionTime ept where ept.position.id = :position_id and ept.entry in (select id from Entry where race.id = :race_id)", EntryPositionTime.class);
            query.setParameter("position_id", position.getId())
                    .setParameter("race_id", race.getId());
            return query.getResultList();
        } else {
            LOG.warn("Got null for race or position");
            return new ArrayList<>();
        }
    }

    public List<EntryPositionTime> getAllTimesForRace(Race race) throws DaoException {
        if (race != null) {
            TypedQuery<EntryPositionTime> query = em.createQuery("FROM EntryPositionTime ept where ept.entry in (select id from Entry where race.id = :race_id) order by ept.position.id", EntryPositionTime.class);
            query.setParameter("race_id", race.getId());
            return query.getResultList();
        } else {
            LOG.warn("Got null for race");
            return new ArrayList<>();
        }
    }

    @Override
    public List<EntryPositionTime> getTimesForRace(Race race) throws DaoException {
        if (race != null) {
            TypedQuery<EntryPositionTime> query = em.createQuery("FROM EntryPositionTime ept where ept.entry in (select e.id from Entry e, Disqualification d where e.race.id = :race_id and e.id != d.entry.id) order by ept.position.id", EntryPositionTime.class);
            query.setParameter("race_id", race.getId());
            return query.getResultList();
        } else {
            LOG.warn("Got null for race");
            return new ArrayList<>();
        }
    }

    @Override
    public void createTime(EntryPositionTime entryPositionTime) throws DaoException {
        try {
            tx.begin();
            if (entryPositionTime.getPosition() != null && entryPositionTime.getEntry() != null) {
                TypedQuery<EntryPositionTime> query = em.createQuery("FROM EntryPositionTime where position.id = :position_id and entry.id = :entry_id", EntryPositionTime.class);
                query.setParameter("position_id", entryPositionTime.getPosition().getId())
                        .setParameter("entry_id", entryPositionTime.getEntry().getId());

                if (query.getResultList().isEmpty()) {
                    entryPositionTime.setSynced(true);
                    em.persist(entryPositionTime);
                    tx.commit();

                } else {
                    String message = "Failed to create time due to existing entryPositionTime with same position " + entryPositionTime.getPosition().getId() + " and entry " + entryPositionTime.getEntry().getId();
                    LOG.warn(message);
                    tx.rollback();
                    throw new DaoException(message);
                }

            } else {
                String message = "Failed to create time due to entryPositionTime missing position or entry ";
                LOG.warn(message);
                tx.rollback();
                throw new DaoException(message);
            }
        } catch (DaoException e) {
            throw e;
        } catch (Exception e) {
            LOG.warn("Failed to create time", e);
            try { tx.rollback(); } catch (Exception se) { LOG.warn("Failed to rollback", se); }
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public void updateTime(EntryPositionTime entryPositionTime) throws DaoException {
        try {
            tx.begin();
            if (entryPositionTime.getPosition() != null && entryPositionTime.getEntry() != null) {
                TypedQuery<EntryPositionTime> query = em.createQuery("FROM EntryPositionTime where position.id = :position_id and entry.id = :entry_id", EntryPositionTime.class);
                query.setParameter("position_id", entryPositionTime.getPosition().getId())
                        .setParameter("entry_id", entryPositionTime.getEntry().getId());

                if (!query.getResultList().isEmpty()) {
                    entryPositionTime.setSynced(true);
                    em.merge(entryPositionTime);
                    tx.commit();

                } else {
                    String message = "Failed to update time as one does not exist for the position " + entryPositionTime.getPosition().getId() + " and entry " + entryPositionTime.getEntry().getId();
                    LOG.warn(message);
                    tx.rollback();
                    throw new DaoException(message);
                }

            } else {
                String message = "Failed to update time due to entryPositionTime missing position or entry ";
                LOG.warn(message);
                tx.rollback();
                throw new DaoException(message);
            }
        } catch (DaoException e) {
            throw e;
        } catch (Exception e) {
            LOG.warn("Failed to update time", e);
            try { tx.rollback(); } catch (Exception se) { LOG.warn("Failed to rollback", se); }
            throw new DaoException(e.getMessage());
        }
    }



}
