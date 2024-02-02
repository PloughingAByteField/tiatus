package org.tiatus.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tiatus.dao.DaoException;
import org.tiatus.dao.EntryDao;
import org.tiatus.entity.Entry;
import org.tiatus.entity.Race;

import jakarta.jms.JMSException;
import java.util.List;

/**
 * Created by johnreynolds on 25/06/2016.
 */
@Service
public class EntryServiceImpl implements EntryService {

    private static final Logger LOG = LoggerFactory.getLogger(EntryServiceImpl.class);

    @Autowired
    protected EntryDao dao;

    @Autowired
    protected MessageSenderService sender;

    @Override
    public Entry getEntryForId(Long id) {
        return dao.getEntryForId(id);
    }

    @Override
    public Entry addEntry(Entry entry, String sessionId) throws ServiceException {
        LOG.debug("Adding entry " + entry);
        try {
            Entry newEntry = dao.addEntry(entry);
            Message message = Message.createMessage(newEntry, MessageType.ADD, sessionId);
            sender.sendMessage(message);
            return newEntry;

        } catch (DaoException e) {
            LOG.warn("Got dao exception");
            throw new ServiceException(e);

        } catch (JMSException e) {
            LOG.warn("Got jms exception", e);
            throw new ServiceException(e);
        }
    }

    @Override
    public void deleteEntry(Entry entry, String sessionId) throws ServiceException {
        LOG.debug("Delete entry " + entry.getId());
        try {
            dao.removeEntry(entry);
            Message message = Message.createMessage(entry, MessageType.DELETE, sessionId);
            sender.sendMessage(message);

        } catch (DaoException e) {
            LOG.warn("Got dao exception");
            throw new ServiceException(e);

        } catch (JMSException e) {
            LOG.warn("Got jms exception", e);
            throw new ServiceException(e);
        }
    }

    @Override
    public Entry updateEntry(Entry entry, String sessionId) throws ServiceException {
        LOG.debug("Update entry " + entry.getId());
        try {
            Entry updated = dao.updateEntry(entry);
            Message message = Message.createMessage(updated, MessageType.UPDATE, sessionId);
            sender.sendMessage(message);

            return updated;

        } catch (DaoException e) {
            LOG.warn("Got dao exception");
            throw new ServiceException(e);

        } catch (JMSException e) {
            LOG.warn("Got jms exception", e);
            throw new ServiceException(e);
        }
    }

    @Override
    public void updateEntries(List<Entry> entries, String sessionId) throws ServiceException {
        for (Entry entry: entries) {
            LOG.debug("Update entry " + entry.getId());
            try {
                dao.updateEntry(entry);
                Message message = Message.createMessage(entries, MessageType.UPDATE, sessionId);
                sender.sendMessage(message);

            } catch (DaoException e) {
                LOG.warn("Got dao exception");
                throw new ServiceException(e);

            } catch (JMSException e) {
                LOG.warn("Got jms exception", e);
                throw new ServiceException(e);
            }
        }
    }

    @Override
    public List<Entry> getEntries() {
        return dao.getEntries();
    }

    @Override
    public List<Entry> getEntriesForRace(Race race) {
        return dao.getEntriesForRace(race);
    }

    @Override
    public void swapEntryNumbers(Entry from, Entry to, String sessionId) throws ServiceException {
        if (from.getRace().getId() != to.getRace().getId()) {
            String warning = "Entries are not in the same race";
            LOG.warn(warning);
            throw new ServiceException(warning);
        }

        if (to.getId() == from.getId()) {
            String warning = "Entries are the same";
            LOG.warn(warning);
            throw new ServiceException(warning);
        }

        // do swap in single transaction updating any position times in the process
        try {
            dao.swapEntryNumbers(from, to);
            Message messageFrom = Message.createMessage(from, MessageType.UPDATE, sessionId);
            sender.sendMessage(messageFrom);
            Message messageTo = Message.createMessage(to, MessageType.UPDATE, sessionId);
            sender.sendMessage(messageTo);

        } catch (DaoException e) {
            LOG.warn("Got dao exception");
            throw new ServiceException(e);

        } catch (JMSException e) {
            LOG.warn("Got jms exception", e);
            throw new ServiceException(e);
        }
    }
}
