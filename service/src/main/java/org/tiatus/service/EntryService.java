package org.tiatus.service;

import org.tiatus.entity.Entry;

import java.util.List;

/**
 * Created by johnreynolds on 25/06/2016.
 */
public interface EntryService {
    /**
     * Get Entry for a given id
     * @return Entry or null
     */
    Entry getEntryForId(Long id);

    /**
     * Add entry
     * @param entry to create
     * @return created entry
     * @throws ServiceException on error
     */
    Entry addEntry(Entry entry) throws ServiceException;

    /**
     * Remove a entry
     * @param entry to remove
     * @throws ServiceException on error
     */
    void deleteEntry(Entry entry) throws ServiceException;

    /**
     * Update a entry
     * @param entry to update
     * @throws ServiceException on error
     */
    void updateEntry(Entry entry) throws ServiceException;

    /**
     * Update list of entries
     * @param entries to update
     * @throws ServiceException on error
     */
    void updateEntries(List<Entry> entries) throws ServiceException;

    /**
     * Get entries
     * @return list of entries
     */
    List<Entry> getEntries();
}
