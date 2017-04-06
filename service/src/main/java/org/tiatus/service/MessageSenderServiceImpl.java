package org.tiatus.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.jms.*;
import java.io.Serializable;

/**
 * Created by johnreynolds on 06/04/2017.
 */
public class MessageSenderServiceImpl implements MessageSenderService {

    private static final Logger LOG = LoggerFactory.getLogger(MessageSenderServiceImpl.class);

    @Resource(mappedName = "java:jboss/exported/jms/queue/message")
    private Queue queue;

    @Inject
    JMSContext context;

    @Override
    public void sendMessage(final Message obj) throws JMSException {
        LOG.debug("Will create object message");
        ObjectMessage message = context.createObjectMessage();
        message.setObject(obj);
        message.setJMSDeliveryMode(DeliveryMode.PERSISTENT);
        context.createProducer().send(queue, message);
    }
}
