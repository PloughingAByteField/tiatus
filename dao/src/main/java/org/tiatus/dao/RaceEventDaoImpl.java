package org.tiatus.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tiatus.entity.*;

import java.util.List;

/**
 * Created by johnreynolds on 10/10/2016.
 */
@Service
public class RaceEventDaoImpl implements RaceEventDao {

    private static final Logger LOG = LoggerFactory.getLogger(RaceEventDaoImpl.class);

    @Autowired
    private RaceEventRepository repository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private EventPositionRepository eventPositionRepository;

    @Override
    public RaceEvent addRaceEvent(RaceEvent raceEvent) throws DaoException {
        LOG.debug("Adding raceEvent " + raceEvent);
        try {
            if (raceEvent.getId() == null) {
                List<RaceEvent> raceEvents = getRaceEventsForRace(raceEvent.getRace());
                for (RaceEvent re: raceEvents) {
                    if (re.getEvent().getName().equals(raceEvent.getEvent().getName())) {
                        String message = "Failed to add raceEvent due to existing raceEvent with event of same name " + raceEvent.getEvent().getName();
                        LOG.warn(message);
                        throw new DaoException(message);
                    }
                }

                eventRepository.save(raceEvent.getEvent());
                for (EventPosition position: raceEvent.getEvent().getPositions()) {
                    position.setEvent(raceEvent.getEvent());
                    position.setPosition(getPositionForId(position.getPositionId()));
                    eventPositionRepository.save(position);
                }
                
                return repository.save(raceEvent);

            } else {
                String message = "Failed to add raceEvent due to existing raceEvent with same id " + raceEvent.getId();
                LOG.warn(message);
                throw new DaoException(message);
            }

        } catch (Exception e) {
            LOG.warn("Failed to persist race event", e);
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public void deleteRaceEvent(RaceEvent raceEvent) throws DaoException {
        try {
            if (repository.existsById(raceEvent.getId())) {
                repository.delete(raceEvent);

            } else {
                LOG.warn("No such event of id " + raceEvent.getId());
            }
        } catch (Exception e) {
            LOG.warn("Failed to delete race event", e);
            throw new DaoException(e.getMessage());
        }
    }


    @Override
    public void updateRaceEvent(RaceEvent raceEvent) throws DaoException {
        try {
            repository.save(raceEvent);

        } catch (Exception e) {
            LOG.warn("Failed to update race event", e);
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public List<RaceEvent> getRaceEventsForRace(Race race) {
        return repository.getRaceEventsForRace(race.getId());
    }

    @Override
    public List<RaceEvent> getRaceEvents() {
        return repository.findByOrderByRaceEventOrder();
    }

    @Override
    public RaceEvent getRaceEventByEvent(Event event) {
        return repository.findByEvent(event);
    }

    @Override
    public RaceEvent getRaceEventForId(Long id) {
        return repository.findById(id).orElse(null);
    }


    private Position getPositionForId(Long id) {
        return positionRepository.findById(id).orElse(null);
    }

}
