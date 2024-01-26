package org.tiatus.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import jakarta.jms.*;

/**
 * Created by johnreynolds on 06/04/2017.
 */
@Service
public class MessageSenderServiceImpl implements MessageSenderService {

    private static final Logger LOG = LoggerFactory.getLogger(MessageSenderServiceImpl.class);

    @Autowired
    JmsTemplate jmsTemplate;


    @Override
    public void sendMessage(final Message message) throws JMSException {
        LOG.debug("Will create object message");
        jmsTemplate.convertAndSend("java:jboss/exported/jms/queue/message", message);
    }
}
