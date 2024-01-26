package org.tiatus.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public void updateEntry(Entry entry) throws DaoException {
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
                if (entry.getCrew() != null) {
                    existing.setCrew(entry.getCrew());
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

                repository.save(existing);

            } else {
                LOG.warn("No such entry of id " + entry.getId());
            }
        } catch (Exception e) {
            LOG.warn("Failed to update entry", e);
            throw new DaoException(e.getMessage());
        }
    }

    /*
     When swapping entry numbers, we must also must update times, penalties and disqualifications applied against the original number
     To do this we use an intermediate entry with id of 0
     */
    @Override
    public synchronized void swapEntryNumbers(Entry from, Entry to) throws DaoException {
        // try {
            Integer fromNumber = from.getNumber();
            Integer fromRaceOrder = from.getRaceOrder();
            Integer toNumber = to.getNumber();
            Integer toRaceOrder = to.getRaceOrder();

            // create intermediate -- needs to be done in separate block as postgres has fk constraints setup
            Entry intermediate = new Entry();
            intermediate.setEvent(from.getEvent());
            intermediate.setRace(from.getRace());
            // intermediate = repository.save(intermediate);

            // // swap from entries in position_time, penalty and disqualification to intermediate entry
            // em.createQuery("UPDATE EntryPositionTime set entry_id = :to_id where entry_id = :from_id")
            //         .setParameter("to_id", intermediate.getId()).setParameter("from_id", from.getId()).executeUpdate();
            // em.createQuery("UPDATE Penalty set entry_id = :to_id where entry_id = :from_id")
            //         .setParameter("to_id", intermediate.getId()).setParameter("from_id", from.getId()).executeUpdate();
            // em.createQuery("UPDATE Disqualification set entry_id = :to_id where entry_id = :from_id")
            //         .setParameter("to_id", intermediate.getId()).setParameter("from_id", from.getId()).executeUpdate();

            // // swap to entries in position_time, penalty and disqualification to the from entry
            // em.createQuery("UPDATE EntryPositionTime set entry_id = :to_id where entry_id = :from_id")
            //         .setParameter("to_id", from.getId()).setParameter("from_id", to.getId()).executeUpdate();
            // em.createQuery("UPDATE Penalty set entry_id = :to_id where entry_id = :from_id")
            //         .setParameter("to_id", from.getId()).setParameter("from_id", to.getId()).executeUpdate();
            // em.createQuery("UPDATE Disqualification set entry_id = :to_id where entry_id = :from_id")
            //         .setParameter("to_id", from.getId()).setParameter("from_id", to.getId()).executeUpdate();

            // // swap intermediate entries in position_time, penalty and disqualification to the to entry
            // em.createQuery("UPDATE EntryPositionTime set entry_id = :to_id where entry_id = :from_id")
            //         .setParameter("to_id", to.getId()).setParameter("from_id", intermediate.getId()).executeUpdate();
            // em.createQuery("UPDATE Penalty set entry_id = :to_id where entry_id = :from_id")
            //         .setParameter("to_id", to.getId()).setParameter("from_id", intermediate.getId()).executeUpdate();
            // em.createQuery("UPDATE Disqualification set entry_id = :to_id where entry_id = :from_id")
            //         .setParameter("to_id", to.getId()).setParameter("from_id", intermediate.getId()).executeUpdate();

            // // delete intermediate
            // em.remove(em.contains(intermediate) ? intermediate : em.merge(intermediate));

            // // swap entry numbers
            // from.setNumber(toNumber);
            // from.setRaceOrder(toRaceOrder);
            // to.setNumber(fromNumber);
            // to.setRaceOrder(fromRaceOrder);

            // em.merge(from);
            // em.merge(to);

        // } catch (Exception e) {
        //     LOG.warn("Failed to swap entries", e);
            // throw new DaoException(e.getMessage());
        // }

        throw new DaoException("Not supported yet");
    }
}
