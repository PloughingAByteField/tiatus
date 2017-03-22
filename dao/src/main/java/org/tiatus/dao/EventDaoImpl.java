package org.tiatus.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.entity.Event;
import org.tiatus.entity.EventPosition;

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
public class EventDaoImpl implements EventDao {

    private static final Logger LOG = LoggerFactory.getLogger(EventDaoImpl.class);

    @PersistenceContext(unitName = "primary")
    protected EntityManager em;

    @Resource
    protected UserTransaction tx;

    @Override
    public Event addEvent(Event event) throws DaoException {
        LOG.debug("Adding event " + event);
        try {
            Event existing = null;
            if (event.getId() != null) {
                existing = em.find(Event.class, event.getId());
            }
            if (existing == null) {
                tx.begin();
                em.persist(event);
                for (EventPosition position: event.getPositions()) {
                    position.setEvent(event);
                    position.setPosition(em.merge(position.getPosition()));
                    em.persist(position);
                }
                tx.commit();

                return event;
            } else {
                String message = "Failed to add event due to existing event with same id " + event.getId();
                LOG.warn(message);
                throw new DaoException(message);
            }
        } catch (Exception e) {
            LOG.warn("Failed to persist event", e);
            try { tx.rollback(); } catch (Exception se) { LOG.warn("Failed to rollback", se); }
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public void deleteEvent(Event event) throws DaoException {
        try {
            Event existing = null;
            if (event.getId() != null) {
                existing = em.find(Event.class, event.getId());
            }
            if (existing != null) {
                tx.begin();
                em.remove(em.contains(event) ? event : em.merge(event));
                tx.commit();
            } else {
                LOG.warn("No such event of id " + event.getId());
            }
        } catch (Exception e) {
            LOG.warn("Failed to delete event", e);
            try { tx.rollback(); } catch (Exception se) { LOG.warn("Failed to rollback", se); }
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public void updateEvent(Event event) throws DaoException {
        try {
            tx.begin();
            em.merge(event);
            tx.commit();
        } catch (Exception e) {
            LOG.warn("Failed to update event", e);
            try { tx.rollback(); } catch (Exception se) { LOG.warn("Failed to rollback", se); }
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public List<Event> getEvents() {
        TypedQuery<Event> query = em.createQuery("FROM Event order by id", Event.class);
        return query.getResultList();
    }

    @Override
    public List<Event> getUnassignedEvents() {
        TypedQuery<Event> query = em.createQuery("FROM Event e where e.id not in (select re.event FROM RaceEvent re)", Event.class);
        return query.getResultList();
    }
}
