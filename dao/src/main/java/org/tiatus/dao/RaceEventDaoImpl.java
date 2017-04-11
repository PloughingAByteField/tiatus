package org.tiatus.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.entity.*;

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
            tx.begin();
            RaceEvent existing = null;
            if (raceEvent.getId() != null) {
                existing = em.find(RaceEvent.class, raceEvent.getId());
            }
            if (existing == null) {
                List<RaceEvent> raceEvents = getRaceEventsForRace(raceEvent.getRace());
                for (RaceEvent re: raceEvents) {
                    if (re.getEvent().getName().equals(raceEvent.getEvent().getName())) {
                        String message = "Failed to add raceEvent due to existing raceEvent with event of same name " + raceEvent.getEvent().getName();
                        LOG.warn(message);
                        throw new DaoException(message);
                    }
                }

                em.persist(raceEvent.getEvent());
                for (EventPosition position: raceEvent.getEvent().getPositions()) {
                    position.setEvent(raceEvent.getEvent());
                    position.setPosition(getPositionForId(position.getPositionId()));
                    em.persist(position);
                }
                RaceEvent merged = em.merge(raceEvent);
                tx.commit();

                return merged;
            } else {
                String message = "Failed to add raceEvent due to existing raceEvent with same id " + raceEvent.getId();
                LOG.warn(message);
                tx.rollback();
                throw new DaoException(message);
            }
        } catch (DaoException e) {
            throw e;
        } catch (Exception e) {
            LOG.warn("Failed to persist race event", e);
            try { tx.rollback(); } catch (SystemException se) { LOG.warn("Failed to rollback", se); }
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public void deleteRaceEvent(RaceEvent raceEvent) throws DaoException {
        try {
            tx.begin();
            RaceEvent existing = null;
            if (raceEvent.getId() != null) {
                existing = em.find(RaceEvent.class, raceEvent.getId());
            }
            if (existing != null) {
                em.remove(existing);
                em.remove(em.contains(existing.getEvent()) ? existing.getEvent() : em.merge(existing.getEvent()));
                tx.commit();
            } else {
                LOG.warn("No such event of id " + raceEvent.getId());
                tx.rollback();
            }
        } catch (Exception e) {
            LOG.warn("Failed to delete race event", e);
            try { tx.rollback(); } catch (SystemException se) { LOG.warn("Failed to rollback", se); }
            throw new DaoException(e.getMessage());
        }
    }


    @Override
    public void updateRaceEvent(RaceEvent raceEvent) throws DaoException {
        try {
            tx.begin();
            em.merge(raceEvent);
            tx.commit();
        } catch (Exception e) {
            LOG.warn("Failed to update race event", e);
            try { tx.rollback(); } catch (SystemException se) { LOG.warn("Failed to rollback", se); }
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public List<RaceEvent> getRaceEventsForRace(Race race) {
        TypedQuery<RaceEvent> query = em.createQuery("FROM RaceEvent re where re.race.id = :race_id order by raceEventOrder", RaceEvent.class);
        return query.setParameter("race_id", race.getId()).getResultList();
    }

    @Override
    public List<RaceEvent> getRaceEvents() {
        TypedQuery<RaceEvent> query = em.createQuery("FROM RaceEvent order by raceEventOrder", RaceEvent.class);
        return query.getResultList();
    }

    @Override
    public RaceEvent getRaceEventByEvent(Event event) {
        TypedQuery<RaceEvent> query = em.createQuery("FROM RaceEvent re where re.event.id = :event_id", RaceEvent.class);
        return query.setParameter("event_id", event.getId()).getSingleResult();
    }

    @Override
    public RaceEvent getRaceEventForId(Long id) {
        return em.find(RaceEvent.class, id);
    }


    private Position getPositionForId(Long id) {
        return em.find(Position.class, id);
    }

}
