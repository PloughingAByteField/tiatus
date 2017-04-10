package org.tiatus.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.dao.DaoException;
import org.tiatus.dao.PositionDao;
import org.tiatus.entity.Position;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.jms.JMSException;
import java.util.List;

/**
 * Created by johnreynolds on 25/06/2016.
 */
@Default
public class PositionServiceImpl implements PositionService {

    private static final Logger LOG = LoggerFactory.getLogger(PositionServiceImpl.class);

    private final PositionDao dao;
    private MessageSenderService sender;

    /**
     * Constructor for service
     * @param dao object injected by cdi
     */
    @Inject
    public PositionServiceImpl(PositionDao dao, MessageSenderService sender) {
       this.dao = dao;
        this.sender = sender;
    }


    @Override
    public Position addPosition(Position position, String sessionId) throws ServiceException {
        LOG.debug("Adding position " + position);
        try {
            Position daoPosition = dao.addPosition(position);
            Message message = Message.createMessage(daoPosition, MessageType.ADD, sessionId);
            sender.sendMessage(message);
            return daoPosition;

        } catch (DaoException e) {
            LOG.warn("Got dao exception");
            throw new ServiceException(e);
        } catch (JMSException e) {
            LOG.warn("Got jms exception", e);
            throw new ServiceException(e);
        }
    }

    @Override
    public void removePosition(Position position, String sessionId) throws ServiceException {
        LOG.debug("Delete position " + position.getId());
        try {
            dao.removePosition(position);
            Message message = Message.createMessage(position, MessageType.DELETE, sessionId);
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
    public Position getPositionForId(Long id) {
        return dao.getPositionForId(id);
    }

    @Override
    public List<Position> getPositions() {
        return dao.getPositions();
    }

    @Override
    public void updatePosition(Position position, String sessionId) throws ServiceException {
        try {
            dao.updatePosition(position);
            Message message = Message.createMessage(position, MessageType.UPDATE, sessionId);
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
