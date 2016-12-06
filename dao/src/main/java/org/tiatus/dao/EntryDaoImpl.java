package org.tiatus.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.entity.Entry;

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
    public List<Entry> getEntries() {
        TypedQuery<Entry> query = em.createQuery("FROM Entry order by race, raceOrder", Entry.class);
        return query.getResultList();
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
        } catch (NotSupportedException | SystemException | HeuristicMixedException | HeuristicRollbackException | RollbackException e) {
            LOG.warn("Failed to persist entry", e.getMessage());
            throw new DaoException(e);
        }
    }

    @Override
    public void removeEntry(Entry entry) throws DaoException {
        try {
            Entry existing = null;
            if (entry.getId() != null) {
                existing = em.find(Entry.class, entry.getId());
            }
            if (existing != null) {
                tx.begin();
                em.remove(em.contains(entry) ? entry : em.merge(entry));
                tx.commit();
            } else {
                LOG.warn("No such entry of id " + entry.getId());
            }
        } catch (NotSupportedException | SystemException | HeuristicMixedException | HeuristicRollbackException | RollbackException e) {
            LOG.warn("Failed to delete entry", e);
            throw new DaoException(e.getMessage());
        }

    }

    @Override
    public void updateEntry(Entry entry) throws DaoException {
        try {
            tx.begin();
            em.merge(entry);
            tx.commit();
        } catch (NotSupportedException | SystemException | HeuristicMixedException | HeuristicRollbackException | RollbackException e) {
            LOG.warn("Failed to update entry", e);
            throw new DaoException(e.getMessage());
        }
    }
}
