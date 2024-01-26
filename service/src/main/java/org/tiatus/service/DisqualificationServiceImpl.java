package org.tiatus.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tiatus.dao.DaoException;
import org.tiatus.dao.DisqualificationDao;
import org.tiatus.entity.Disqualification;

import jakarta.jms.JMSException;

import java.util.List;

/**
 * Created by johnreynolds on 01/03/2017.
 */
@Service
public class DisqualificationServiceImpl implements DisqualificationService {
    private static final Logger LOG = LoggerFactory.getLogger(DisqualificationServiceImpl.class);

    @Autowired
    protected DisqualificationDao dao;

    @Autowired
    protected MessageSenderService sender;

    @Override
    public Disqualification addDisqualification(Disqualification disqualification, String sessionId) throws ServiceException {
        LOG.debug("Adding disqualification for entry " + disqualification.getEntry().getId());
        try {
            Disqualification q = dao.addDisqualification(disqualification);
            Message message = Message.createMessage(q, MessageType.ADD, sessionId);
            sender.sendMessage(message);
            return q;

        } catch (DaoException e) {
            LOG.warn("Got dao exception");
            throw new ServiceException(e);

        } catch (JMSException e) {
            LOG.warn("Got jms exception", e);
            throw new ServiceException(e);
        }
    }

    @Override
    public void deleteDisqualification(Disqualification disqualification, String sessionId) throws ServiceException {
        LOG.debug("Delete disqualification " + disqualification.getId());
        try {
            dao.removeDisqualification(disqualification);
            Message message = Message.createMessage(disqualification, MessageType.DELETE, sessionId);
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
    public Disqualification updateDisqualification(Disqualification disqualification, String sessionId) throws ServiceException {
        LOG.debug("Update disqualification " + disqualification.getId());
        try {
            Disqualification updated = dao.updateDisqualification(disqualification);
            Message message = Message.createMessage(disqualification, MessageType.UPDATE, sessionId);
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
    public Disqualification getDisqualificationForId(Long id) {
        return dao.getDisqualificationForId(id);
    }

    @Override
    public List<Disqualification> getDisqualifications() {
        return dao.getDisqualifications();
    }
}
