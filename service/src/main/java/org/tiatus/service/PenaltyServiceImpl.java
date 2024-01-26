package org.tiatus.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tiatus.dao.DaoException;
import org.tiatus.dao.PenaltyDao;
import org.tiatus.entity.Penalty;

import jakarta.jms.JMSException;

import java.util.List;

/**
 * Created by johnreynolds on 01/03/2017.
 */
@Service
public class PenaltyServiceImpl implements PenaltyService {
    private static final Logger LOG = LoggerFactory.getLogger(PenaltyServiceImpl.class);

    @Autowired
    private PenaltyDao dao;

    @Autowired
    protected MessageSenderService sender;

    @Override
    public Penalty addPenalty(Penalty penalty, String sessionId) throws ServiceException {
        LOG.debug("Adding penalty for entry " + penalty.getEntry().getId());
        try {
            Penalty daoPenalty = dao.addPenalty(penalty);
            Message message = Message.createMessage(daoPenalty, MessageType.ADD, sessionId);
            sender.sendMessage(message);
            return daoPenalty;

        } catch (DaoException e) {
            LOG.warn("Got dao exception");
            throw new ServiceException(e);
            
        } catch (JMSException e) {
            LOG.warn("Got jms exception", e);
            throw new ServiceException(e);
        }
    }

    @Override
    public void deletePenalty(Penalty penalty, String sessionId) throws ServiceException {
        LOG.debug("Delete penalty " + penalty.getId());
        try {
            dao.removePenalty(penalty);
            Message message = Message.createMessage(penalty, MessageType.DELETE, sessionId);
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
    public void updatePenalty(Penalty penalty, String sessionId) throws ServiceException {
        LOG.debug("Update penalty " + penalty.getId());
        try {
            dao.updatePenalty(penalty);
            Message message = Message.createMessage(penalty, MessageType.UPDATE, sessionId);
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
    public List<Penalty> getPenalties() {
        return dao.getPenalties();
    }

    @Override
    public Penalty getPenaltyForId(Long id) {
        return dao.getPenaltyForId(id);
    }
}
