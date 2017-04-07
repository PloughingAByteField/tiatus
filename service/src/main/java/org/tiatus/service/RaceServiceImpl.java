package org.tiatus.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.dao.DaoException;
import org.tiatus.dao.RaceDao;
import org.tiatus.entity.Race;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.jms.JMSException;
import java.util.List;

/**
 * Created by johnreynolds on 25/06/2016.
 */
@Default
public class RaceServiceImpl implements RaceService {

    private static final Logger LOG = LoggerFactory.getLogger(RaceServiceImpl.class);

    private final RaceDao dao;
    private MessageSenderService sender;

    /**
     * Constructor for service
     * @param dao object injected by cdi
     */
    @Inject
    public RaceServiceImpl(RaceDao dao, MessageSenderService sender) {
       this.dao = dao;
       this.sender = sender;
    }

    @Override
    public Race getRaceForId(Long id) {
        return dao.getRaceForId(id);
    }

    private Message createMessage(Race race, MessageType type, String sessionId) {
        Message message = new Message();
        message.setData(race);
        message.setType(type);
        message.setSessionId(sessionId);
        return message;
    }

    @Override
    public Race addRace(Race race, String sessionId) throws ServiceException {
        LOG.debug("Adding race " + race);
        try {
            Race r = dao.addRace(race);
            Message message = createMessage(race, MessageType.ADD, sessionId);
            sender.sendMessage(message);
            return r;

        } catch (DaoException e) {
            LOG.warn("Got dao exception");
            throw new ServiceException(e);
        } catch (JMSException e) {
            LOG.warn("Got jms exception");
            throw new ServiceException(e);
        }
    }

    @Override
    public void deleteRace(Race race, String sessionId) throws ServiceException {
        LOG.debug("Delete race " + race.getId());
        try {
            dao.removeRace(race);
            Message message = createMessage(race, MessageType.DELETE, sessionId);
            sender.sendMessage(message);
        } catch (DaoException e) {
            LOG.warn("Got dao exception");
            throw new ServiceException(e);
        } catch (JMSException e) {
            LOG.warn("Got jms exception");
            throw new ServiceException(e);
        }
    }

    @Override
    public void updateRace(Race race, String sessionId) throws ServiceException {
        LOG.debug("Delete race " + race.getId());
        try {
            dao.updateRace(race);
            Message message = createMessage(race, MessageType.UPDATE, sessionId);
            sender.sendMessage(message);
        } catch (DaoException e) {
            LOG.warn("Got dao exception");
            throw new ServiceException(e);
        } catch (JMSException e) {
            LOG.warn("Got jms exception");
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Race> getRaces() {
        return dao.getRaces();
    }
}
