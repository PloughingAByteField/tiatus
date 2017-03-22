package org.tiatus.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.entity.Event;
import org.tiatus.entity.EventPosition;
import org.tiatus.entity.Race;
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
                List<RaceEvent> raceEvents = getRaceEventsForRace(raceEvent.getRace());
                for (RaceEvent re: raceEvents) {
                    if (re.getEvent().getName().equals(raceEvent.getEvent().getName())) {
                        String message = "Failed to add raceEvent due to existing raceEvent with event of same name " + raceEvent.getEvent().getName();
                        LOG.warn(message);
                        throw new DaoException(message);
                    }
                }
                tx.begin();
                em.persist(raceEvent.getEvent());
                for (EventPosition position: raceEvent.getEvent().getPositions()) {
                    position.setEvent(raceEvent.getEvent());
                    position.setPosition(em.merge(position.getPosition()));
                    em.persist(position);
                }
                RaceEvent merged = em.merge(raceEvent);
                tx.commit();

                return merged;
            } else {
                String message = "Failed to add raceEvent due to existing raceEvent with same id " + raceEvent.getId();
                LOG.warn(message);
                throw new DaoException(message);
            }
        } catch (Exception e) {
            LOG.warn("Failed to persist race event", e);
            try { tx.rollback(); } catch (SystemException se) { LOG.warn("Failed to rollback", se); }
            throw new DaoException(e.getMessage());
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

}
