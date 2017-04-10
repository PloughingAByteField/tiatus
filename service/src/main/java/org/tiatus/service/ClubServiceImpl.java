package org.tiatus.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.dao.DaoException;
import org.tiatus.dao.ClubDao;
import org.tiatus.entity.Club;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.jms.JMSException;
import java.util.List;

/**
 * Created by johnreynolds on 25/06/2016.
 */
@Default
public class ClubServiceImpl implements ClubService {

    private static final Logger LOG = LoggerFactory.getLogger(ClubServiceImpl.class);

    private final ClubDao dao;
    private MessageSenderService sender;

    /**
     * Constructor for service
     * @param dao object injected by cdi
     */
    @Inject
    public ClubServiceImpl(ClubDao dao, MessageSenderService sender) {
       this.dao = dao;
        this.sender = sender;
    }

    @Override
    public Club addClub(Club club, String sessionId) throws ServiceException {
        LOG.debug("Adding club " + club);
        try {
            Club c = dao.addClub(club);
            Message message = Message.createMessage(club, MessageType.ADD, sessionId);
            sender.sendMessage(message);
            return c;

        } catch (DaoException e) {
            LOG.warn("Got dao exception");
            throw new ServiceException(e);
        } catch (JMSException e) {
            LOG.warn("Got jms exception", e);
            throw new ServiceException(e);
        }
    }

    @Override
    public void deleteClub(Club club, String sessionId) throws ServiceException {
        LOG.debug("Delete club " + club.getId());
        try {
            dao.removeClub(club);
            Message message = Message.createMessage(club, MessageType.DELETE, sessionId);
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
    public void updateClub(Club club, String sessionId) throws ServiceException {
        LOG.debug("Update club " + club.getId());
        try {
            dao.updateClub(club);
            Message message = Message.createMessage(club, MessageType.UPDATE, sessionId);
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
    public List<Club> getClubs() {
        return dao.getClubs();
    }
}
