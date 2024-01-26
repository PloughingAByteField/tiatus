package org.tiatus.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tiatus.dao.DaoException;
import org.tiatus.dao.RacePositionTemplateDao;
import org.tiatus.entity.RacePositionTemplate;
import org.tiatus.entity.RacePositionTemplateEntry;

import jakarta.jms.JMSException;

import java.util.List;

/**
 * Created by johnreynolds on 09/03/2017.
 */
@Service
public class RacePositionTemplateServiceImpl implements RacePositionTemplateService {
    private static final Logger LOG = LoggerFactory.getLogger(RacePositionTemplateServiceImpl.class);

    @Autowired
    protected RacePositionTemplateDao dao;

    @Autowired
    protected MessageSenderService sender;

    @Override
    public RacePositionTemplate addRacePositionTemplate(RacePositionTemplate template, String sessionId) throws ServiceException {
        LOG.debug("Adding template " + template.getName());
        try {
            RacePositionTemplate rpt = dao.addRacePositionTemplate(template);
            Message message = Message.createMessage(rpt, MessageType.ADD, sessionId);
            sender.sendMessage(message);
            return rpt;

        } catch (DaoException e) {
            LOG.warn("Got dao exception");
            throw new ServiceException(e);
            
        } catch (JMSException e) {
            LOG.warn("Got jms exception", e);
            throw new ServiceException(e);
        }
    }

    @Override
    public void deleteRacePositionTemplate(RacePositionTemplate template, String sessionId) throws ServiceException {
        LOG.debug("Delete template " + template.getId());
        try {
            dao.deleteRacePositionTemplate(template);
            Message message = Message.createMessage(template, MessageType.DELETE, sessionId);
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
    public void updateRacePositionTemplate(RacePositionTemplate template, String sessionId) throws ServiceException {
        LOG.debug("Update template " + template.getId());
        try {
            dao.updateRacePositionTemplate(template);
            Message message = Message.createMessage(template, MessageType.UPDATE, sessionId);
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
    public List<RacePositionTemplate> getRacePositionTemplates() {
        return dao.getRacePositionTemplates();
    }

    @Override
    public RacePositionTemplate getTemplateForId(Long id) {
        return dao.getTemplateForId(id);
    }

    @Override
    public RacePositionTemplateEntry addTemplateEntry(RacePositionTemplateEntry entry, String sessionId) throws ServiceException {
        try {
            RacePositionTemplateEntry rpte = dao.addTemplateEntry(entry);
            Message message = Message.createMessage(rpte, MessageType.ADD, sessionId);
            sender.sendMessage(message);
            return rpte;

        } catch (DaoException e) {
            LOG.warn("Got dao exception");
            throw new ServiceException(e);

        } catch (JMSException e) {
            LOG.warn("Got jms exception", e);
            throw new ServiceException(e);
        }
    }

    @Override
    public void deleteTemplateEntry(RacePositionTemplateEntry entry, String sessionId) throws ServiceException {
        try {
            dao.deleteTemplateEntry(entry);
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
    public void updateTemplateEntry(RacePositionTemplateEntry entry, String sessionId) throws ServiceException {
        try {
            dao.updateTemplateEntry(entry);
            Message message = Message.createMessage(entry, MessageType.UPDATE, sessionId);
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
