package org.tiatus.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tiatus.dao.DaoException;
import org.tiatus.dao.ClubDao;
import org.tiatus.entity.Club;

import jakarta.jms.JMSException;

import java.util.List;

/**
 * Created by johnreynolds on 25/06/2016.
 */
@Service
public class ClubServiceImpl implements ClubService {

    private static final Logger LOG = LoggerFactory.getLogger(ClubServiceImpl.class);

    @Autowired
    protected ClubDao dao;

    @Autowired
    protected MessageSenderService sender;

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
    public Club updateClub(Club club, String sessionId) throws ServiceException {
        LOG.debug("Update club " + club.getId());
        try {
            Club updated = dao.updateClub(club);
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
    public Club getClubForId(Long id) {
        return dao.getClubForId(id);
    }

    @Override
    public List<Club> getClubs() {
        return dao.getClubs();
    }
}
