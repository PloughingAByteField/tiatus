package org.tiatus.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tiatus.entity.Event;
import org.tiatus.entity.EventPosition;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnreynolds on 10/10/2016.
 */
@Service
public class EventDaoImpl implements EventDao {

    private static final Logger LOG = LoggerFactory.getLogger(EventDaoImpl.class);

    @Autowired
    EventRepository repository;

    @Autowired
    EventPositionRepository eventPositionRepository;

    /*
        Only unassigned events are created here, race assigned events are created in RaceEvent
     */
    @Override
    public Event addEvent(Event event) throws DaoException {
        LOG.debug("Adding event " + event);
        try {
            if (event.getId() == null) {
                List<Event> unassignedEvents = getUnassignedEvents();
                for (Event unassigned: unassignedEvents) {
                    if (unassigned.getName().equals(event.getName())) {
                        String message = "Failed to add event due to existing unassigned event with same name " + event.getName();
                        LOG.warn(message);
                        throw new DaoException(message);
                    }
                }

                return repository.save(event);

            } else {
                String message = "Failed to add event due to existing event with same id " + event.getId();
                LOG.warn(message);
                throw new DaoException(message);
            }

        } catch (Exception e) {
            LOG.warn("Failed to persist event", e);
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public void deleteEvent(Event event) throws DaoException {
        try {
            if (repository.existsById(event.getId())) {
                repository.delete(event);

            } else {
                String message = "No such event of id " + event.getId();
                LOG.warn(message);
                throw new DaoException(message);
            }

        } catch (Exception e) {
            LOG.warn("Failed to delete event", e);
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public Event updateEvent(Event event) throws DaoException {
        try {
            List<EventPosition> addedPositions = new ArrayList<>();
            List<EventPosition> deletedPositions = new ArrayList<>();
            List<EventPosition> updatedPositions = new ArrayList<>();
            
            Event existing = repository.findById(event.getId()).orElse(null);
            if (existing != null) {
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
                    eventPositionRepository.save(position);
                }
                for (EventPosition position: deletedPositions) {
                    eventPositionRepository.delete(position);
                }
                for (EventPosition position: updatedPositions) {
                    eventPositionRepository.save(position);
                }
                
                if (!existing.getName().equals(event.getName())) {
                    existing.setName(event.getName());
                }

                return repository.save(existing);

            } else {
                String message = "No such event of id " + event.getId();
                LOG.warn(message);
                throw new DaoException(message);
            }
            
        } catch (Exception e) {
            LOG.warn("Failed to update event", e);
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public List<Event> getEvents() {
        return repository.findAllByOrderById();
    }

    @Override
    public List<Event> getUnassignedEvents() {
        return repository.getUnassignedEvents();
    }

    @Override
    public Event getEventForId(Long id) {
        return repository.findById(id).orElse(null);
    }
}
