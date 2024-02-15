package org.tiatus.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tiatus.entity.*;

import java.util.List;

/**
 * Created by johnreynolds on 19/06/2016.
 */
@Service
public class EntryDaoImpl implements EntryDao {

    private static final Logger LOG = LoggerFactory.getLogger(EntryDaoImpl.class);

    @Autowired
    private EntryRepository repository;

    @Autowired
    private DisqualificationRepository disqualificationRepository;

    @Autowired
    private PenaltyRepository penaltyRepository;

    @Autowired
    private EntryPositionTimeRepository entryPositionTimeRepository;

    @Override
    public Entry getEntryForId(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<Entry> getEntries() {
        return repository.findAll();
    }

    @Override
    public List<Entry> getEntriesForRace(Race race) {
        return repository.findByRaceOrderByRaceOrder(race);
    }

    @Override
    public Entry addEntry(Entry entry) throws DaoException {
        LOG.debug("Adding entry " + entry);
        try {
            boolean exists = false;
            if (entry.getId() != null) {
                exists = repository.existsById(entry.getId());
            }
            if (!exists) {
                return repository.save(entry);

            } else {
                String message = "Failed to add entry due to existing entry with same id " + entry.getId();
                LOG.warn(message);
                throw new DaoException(message);
            }

        } catch (Exception e) {
            LOG.warn("Failed to persist entry", e);
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public void removeEntry(Entry entry) throws DaoException {
        try {
            boolean exists = false;
            if (entry.getId() != null) {
                exists = repository.existsById(entry.getId());
            }
            if (exists) {
                entryPositionTimeRepository.deletePostionTimesForEntry(entry.getId());
                repository.delete(entry);

            } else {
                LOG.warn("No such entry of id " + entry.getId());
            }

        } catch (Exception e) {
            LOG.warn("Failed to remove entry", e);
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public void removeEntriesForRace(Race race) throws DaoException {
        LOG.debug("Delete entries for race " + race.getName());
        entryPositionTimeRepository.deletePostionTimesForRace(race.getId());
        repository.deleteClubEntriesXrefForRace(race.getId());
        repository.deleteDisqualificationsForRace(race.getId());
        repository.deletePenaltiesForRace(race.getId());
        repository.deleteEntriesForRace(race.getId());
    }

    @Override
    public Entry updateEntry(Entry entry) throws DaoException {
        try {
            Entry existing = null;
            if (entry.getId() != null) {
                existing = repository.findById(entry.getId()).orElse(null);
            }
            if (existing != null) {
                if (entry.getNumber() != null) {
                    existing.setNumber(entry.getNumber());
                }
                if (entry.getRace() != null) {
                    existing.setRace(entry.getRace());
                }
                if (entry.getEvent() != null) {
                    existing.setEvent(entry.getEvent());
                }
                if (entry.getName() != null) {
                    existing.setName(entry.getName());
                }
                if (entry.isFixedNumber()) {
                    existing.setFixedNumber(entry.isFixedNumber());
                }
                if (entry.getClubs() != null) {
                    existing.setClubs(entry.getClubs());
                }
                if (entry.getRaceOrder() != null) {
                    existing.setRaceOrder(entry.getRaceOrder());
                }

                return repository.save(existing);

            } else {
                LOG.warn("No such entry of id " + entry.getId());
            }
        } catch (Exception e) {
            LOG.warn("Failed to update entry", e);
            throw new DaoException(e.getMessage());
        }

        return entry;
    }

    /*
     When swapping entry numbers, we must also must update times, penalties and disqualifications applied against the original number
     To do this we use an intermediate entry with id of 0
     */
    @Transactional
    @Override
    public synchronized void swapEntryNumbers(Entry from, Entry to) throws DaoException {
        try {
            Integer fromNumber = from.getNumber();
            Integer fromRaceOrder = from.getRaceOrder();
            Integer toNumber = to.getNumber();
            Integer toRaceOrder = to.getRaceOrder();

            // create intermediate -- needs to be done in separate block as postgres has fk constraints setup
            Entry intermediate = new Entry();
            intermediate.setEvent(from.getEvent());
            intermediate.setRace(from.getRace());
            intermediate = repository.save(intermediate);

            // swap from entries in position_time, penalty and disqualification to intermediate entry
            entryPositionTimeRepository.updatePostionTimeEntryId(from.getId(), intermediate.getId());
            penaltyRepository.updatePenaltyEntryId(from.getId(), intermediate.getId());
            disqualificationRepository.updateDisqualificationEntryId(from.getId(), intermediate.getId());

            // swap to entries in position_time, penalty and disqualification to the from entry
            entryPositionTimeRepository.updatePostionTimeEntryId(to.getId(), from.getId());
            penaltyRepository.updatePenaltyEntryId(to.getId(), from.getId());
            disqualificationRepository.updateDisqualificationEntryId(to.getId(), from.getId());

            // swap intermediate entries in position_time, penalty and disqualification to the to entry
            entryPositionTimeRepository.updatePostionTimeEntryId(intermediate.getId(), to.getId());
            penaltyRepository.updatePenaltyEntryId(intermediate.getId(), to.getId());
            disqualificationRepository.updateDisqualificationEntryId(intermediate.getId(), to.getId());

            // delete intermediate
            repository.delete(intermediate);

            // swap entry numbers
            from.setNumber(toNumber);
            from.setRaceOrder(toRaceOrder);
            to.setNumber(fromNumber);
            to.setRaceOrder(fromRaceOrder);

            repository.save(from);
            repository.save(to);

        } catch (Exception e) {
            LOG.warn("Failed to swap entries", e);
            throw new DaoException(e.getMessage());
        }

    }
}
