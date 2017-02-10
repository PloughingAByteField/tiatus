package org.tiatus.dao;

import org.tiatus.entity.Entry;

import java.util.List;

/**
 * Created by johnreynolds on 19/06/2016.
 */
public interface EntryDao {
    /**
     * Get Entry for a given id
     * @return Entry or null
     */
    Entry getEntryForId(Long id);

    /**
     * Get Entries
     * @return a list of Entries
     */
    List<Entry> getEntries();

    /**
     * Add a new Entry
     * @param entry Entry to add
     * @return Entry Added entry
     * @throws DaoException on error
     */
    Entry addEntry(Entry entry) throws DaoException;

    /**
     * Remove a Entry
     * @param entry Entry to remove
     * @throws DaoException on error
     */
    void removeEntry(Entry entry) throws DaoException;

    /**
     * Update a Entry
     * @param entry Entry to update
     * @throws DaoException on error
     */
    void updateEntry(Entry entry) throws DaoException;

}
