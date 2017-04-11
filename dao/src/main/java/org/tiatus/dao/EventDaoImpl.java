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
import java.util.ArrayList;
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

    /*
        Only unassigned events are created here, race assigned events are created in RaceEvent
     */
    @Override
    public Event addEvent(Event event) throws DaoException {
        LOG.debug("Adding event " + event);
        try {
            tx.begin();
            Event existing = null;
            if (event.getId() != null) {
                existing = em.find(Event.class, event.getId());
            }
            if (existing == null) {
                // check to see if name is already used in unassigned
                List<Event> unassignedEvents = getUnassignedEvents();
                for (Event unassigned: unassignedEvents) {
                    if (unassigned.getName().equals(event.getName())) {
                        String message = "Failed to add event due to existing unassigned event with same name " + event.getName();
                        LOG.warn(message);
                        throw new DaoException(message);
                    }
                }

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
                tx.rollback();
                throw new DaoException(message);
            }
        } catch (DaoException e) {
            throw e;
        } catch (Exception e) {
            LOG.warn("Failed to persist event", e);
            try { tx.rollback(); } catch (Exception se) { LOG.warn("Failed to rollback", se); }
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public void deleteEvent(Event event) throws DaoException {
        try {
            tx.begin();
            Event existing = null;
            if (event.getId() != null) {
                existing = em.find(Event.class, event.getId());
            }
            if (existing != null) {
                em.remove(em.contains(event) ? event : em.merge(event));
                tx.commit();
            } else {
                LOG.warn("No such event of id " + event.getId());
                tx.rollback();
            }
        } catch (Exception e) {
            LOG.warn("Failed to delete event", e);
            try { tx.rollback(); } catch (Exception se) { LOG.warn("Failed to rollback", se); }
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public Event updateEvent(Event event) throws DaoException {
        try {
            tx.begin();
            List<EventPosition> addedPositions = new ArrayList<>();
            List<EventPosition> deletedPositions = new ArrayList<>();
            List<EventPosition> updatedPositions = new ArrayList<>();
            Event existing = em.find(Event.class, event.getId());
            for (EventPosition position: existing.getPositions()) {
                boolean foundInUpdatedList = false;
                for (EventPosition updatedPosition: event.getPositions()) {
                    if (position.getPositionOrder() == updatedPosition.getPositionOrder()) {
                        foundInUpdatedList = true;
                        // has position changed
                        if (position.getPosition().getId() != updatedPosition.getPosition().getId()) {
                            position.setPosition(updatedPosition.getPosition());
                            updatedPositions.add(position);
                        }
                        break;
                    }
                }
                if (!foundInUpdatedList) {
                    // lost position
                    deletedPositions.add(position);
                }
            }
            for (EventPosition updatedPosition: event.getPositions()) {
                boolean foundInExistingList = false;
                for (EventPosition position: existing.getPositions()) {
                    if (position.getPositionOrder() == updatedPosition.getPositionOrder()) {
                        foundInExistingList = true;
                        break;
                    }
                }
                if (!foundInExistingList) {
                    // added position
                    updatedPosition.setEvent(existing);
                    addedPositions.add(updatedPosition);
                }
            }

            for (EventPosition position: addedPositions) {
                em.persist(position);
            }
            for (EventPosition position: deletedPositions) {
                em.remove(em.contains(position) ? position : em.merge(position));
            }
            for (EventPosition position: updatedPositions) {
                em.merge(position);
            }
            if (!existing.getName().equals(event.getName())) {
                existing.setName(event.getName());
                em.merge(existing);
            }
            tx.commit();

            existing = em.find(Event.class, event.getId());
            return existing;
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

    @Override
    public Event getEventForId(Long id) {
        return em.find(Event.class, id);
    }
}
