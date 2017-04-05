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
 * Created by johnreynolds on 19/06/2016.
 */
@Default
public class EntryDaoImpl implements EntryDao {

    private static final Logger LOG = LoggerFactory.getLogger(EntryDaoImpl.class);

    @PersistenceContext(unitName = "primary")
    protected EntityManager em;

    @Resource
    protected UserTransaction tx;

    @Override
    public Entry getEntryForId(Long id) {
        return em.find(Entry.class, id);
    }

    @Override
    public List<Entry> getEntries() {
        TypedQuery<Entry> query = em.createQuery("FROM Entry order by race, raceOrder", Entry.class);
        return query.getResultList();
    }

    @Override
    public List<Entry> getEntriesForRace(Race race) {
        TypedQuery<Entry> query = em.createQuery("FROM Entry where race_id = :race_id order by raceOrder", Entry.class);
        return query.setParameter("race_id", race.getId()).getResultList();
    }

    @Override
    public Entry addEntry(Entry entry) throws DaoException {
        LOG.debug("Adding entry " + entry);
        try {
            Entry existing = null;
            if (entry.getId() != null) {
                existing = em.find(Entry.class, entry.getId());
            }
            if (existing == null) {
                tx.begin();
                Entry merged = em.merge(entry);
                tx.commit();

                return merged;
            } else {
                String message = "Failed to add entry due to existing entry with same id " + entry.getId();
                LOG.warn(message);
                throw new DaoException(message);
            }
        } catch (Exception e) {
            LOG.warn("Failed to persist entry", e);
            try { tx.rollback(); } catch (Exception se) { LOG.warn("Failed to rollback", se); }
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public void removeEntry(Entry entry) throws DaoException {
        try {
            tx.begin();
            Entry existing = null;
            if (entry.getId() != null) {
                existing = em.find(Entry.class, entry.getId());
            }
            if (existing != null) {

                em.remove(em.contains(entry) ? entry : em.merge(entry));
                tx.commit();
            } else {
                LOG.warn("No such entry of id " + entry.getId());
            }
        } catch (Exception e) {
            LOG.warn("Failed to remove entry", e);
            try { tx.rollback(); } catch (Exception se) { LOG.warn("Failed to rollback", se); }
            throw new DaoException(e.getMessage());
        }

    }

    @Override
    public void updateEntry(Entry entry) throws DaoException {
        try {
            tx.begin();
            Entry existing = null;
            if (entry.getId() != null) {
                existing = em.find(Entry.class, entry.getId());
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

                em.merge(existing);
                tx.commit();
            } else {
                LOG.warn("No such entry of id " + entry.getId());
            }
        } catch (Exception e) {
            LOG.warn("Failed to update entry", e);
            try { tx.rollback(); } catch (Exception se) { LOG.warn("Failed to rollback", se); }
            throw new DaoException(e.getMessage());
        }
    }

    /*
     When swapping entry numbers, we must also must update times, penalties and disqualifications applied against the original number
     To do this we use an intermediate entry with id of 0
     */
    @Override
    public synchronized void swapEntryNumbers(Entry from, Entry to) throws DaoException {
        try {
            Integer fromNumber = from.getNumber();
            Integer fromRaceOrder = from.getRaceOrder();
            Integer toNumber = to.getNumber();
            Integer toRaceOrder = to.getRaceOrder();

            tx.begin();
            // create intermediate -- needs to be done in separate block as postgres has fk constraints setup
            Entry intermediate = new Entry();
            intermediate.setEvent(from.getEvent());
            intermediate.setRace(from.getRace());
            intermediate = em.merge(intermediate);
            tx.commit();

            tx.begin();
            // swap from entries in position_time, penalty and disqualification to intermediate entry
            em.createQuery("UPDATE EntryPositionTime set entry_id = :to_id where entry_id = :from_id")
                    .setParameter("to_id", intermediate.getId()).setParameter("from_id", from.getId()).executeUpdate();
            em.createQuery("UPDATE Penalty set entry_id = :to_id where entry_id = :from_id")
                    .setParameter("to_id", intermediate.getId()).setParameter("from_id", from.getId()).executeUpdate();
            em.createQuery("UPDATE Disqualification set entry_id = :to_id where entry_id = :from_id")
                    .setParameter("to_id", intermediate.getId()).setParameter("from_id", from.getId()).executeUpdate();

            // swap to entries in position_time, penalty and disqualification to the from entry
            em.createQuery("UPDATE EntryPositionTime set entry_id = :to_id where entry_id = :from_id")
                    .setParameter("to_id", from.getId()).setParameter("from_id", to.getId()).executeUpdate();
            em.createQuery("UPDATE Penalty set entry_id = :to_id where entry_id = :from_id")
                    .setParameter("to_id", from.getId()).setParameter("from_id", to.getId()).executeUpdate();
            em.createQuery("UPDATE Disqualification set entry_id = :to_id where entry_id = :from_id")
                    .setParameter("to_id", from.getId()).setParameter("from_id", to.getId()).executeUpdate();

            // swap intermediate entries in position_time, penalty and disqualification to the to entry
            em.createQuery("UPDATE EntryPositionTime set entry_id = :to_id where entry_id = :from_id")
                    .setParameter("to_id", to.getId()).setParameter("from_id", intermediate.getId()).executeUpdate();
            em.createQuery("UPDATE Penalty set entry_id = :to_id where entry_id = :from_id")
                    .setParameter("to_id", to.getId()).setParameter("from_id", intermediate.getId()).executeUpdate();
            em.createQuery("UPDATE Disqualification set entry_id = :to_id where entry_id = :from_id")
                    .setParameter("to_id", to.getId()).setParameter("from_id", intermediate.getId()).executeUpdate();

            // delete intermediate
            em.remove(em.contains(intermediate) ? intermediate : em.merge(intermediate));

            // swap entry numbers
            from.setNumber(toNumber);
            from.setRaceOrder(toRaceOrder);
            to.setNumber(fromNumber);
            to.setRaceOrder(fromRaceOrder);

            em.merge(from);
            em.merge(to);

            tx.commit();
        } catch (Exception e) {
            LOG.warn("Failed to swap entries", e);
            try { tx.rollback(); } catch (Exception se) { LOG.warn("Failed to rollback", se); }
            throw new DaoException(e.getMessage());
        }
    }
}
