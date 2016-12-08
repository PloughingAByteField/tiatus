package org.tiatus.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.dao.DaoException;
import org.tiatus.dao.EntryDao;
import org.tiatus.entity.Entry;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.util.List;

/**
 * Created by johnreynolds on 25/06/2016.
 */
@Default
public class EntryServiceImpl implements EntryService {

    private static final Logger LOG = LoggerFactory.getLogger(EntryServiceImpl.class);

    private final EntryDao dao;

    /**
     * Constructor for service
     * @param dao object injected by cdi
     */
    @Inject
    public EntryServiceImpl(EntryDao dao) {
       this.dao = dao;
    }

    @Override
    public Entry addEntry(Entry entry) throws ServiceException {
        LOG.debug("Adding entry " + entry);
        try {
            return dao.addEntry(entry);

        } catch (DaoException e) {
            LOG.warn("Got dao exception");
            throw new ServiceException(e);
        }
    }

    @Override
    public void deleteEntry(Entry entry) throws ServiceException {
        LOG.debug("Delete entry " + entry.getId());
        try {
            dao.removeEntry(entry);
        } catch (DaoException e) {
            LOG.warn("Got dao exception");
            throw new ServiceException(e);
        }
    }

    @Override
    public void updateEntry(Entry entry) throws ServiceException {
        LOG.debug("Update club " + entry.getId());
        try {
            dao.updateEntry(entry);
        } catch (DaoException e) {
            LOG.warn("Got dao exception");
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Entry> getEntries() {
        return dao.getEntries();
    }
}
