package org.tiatus.service;

import org.tiatus.entity.Entry;

import java.util.List;

/**
 * Created by johnreynolds on 25/06/2016.
 */
public interface EntryService {
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
     * Get entries
     * @return list of entries
     */
    List<Entry> getEntries();
}
