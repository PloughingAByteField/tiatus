package org.tiatus.dao;

import org.tiatus.entity.Entry;
import org.tiatus.entity.Race;

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
     * Get Entries for race
     * @return a list of Entries
     */
    List<Entry> getEntriesForRace(Race race);


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
     * Remove Entries for race
     * @throws DaoException on error
     */
    void removeEntriesForRace(Race race) throws DaoException;

    /**
     * Update a Entry
     * @param entry Entry to update
     * @throws DaoException on error
     */
    Entry updateEntry(Entry entry) throws DaoException;

    /**
     * Swap the numbers of two entries
     * @param from entry to switch number from
     * @param to entry to switch number to
     * @throws DaoException on error
     */
    void swapEntryNumbers(Entry from, Entry to) throws DaoException;
}
