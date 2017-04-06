package org.tiatus.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

/**
 * Created by johnreynolds on 06/04/2017.
 */

@MessageDriven(mappedName = "Messages", activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "java:jboss/exported/jms/queue/message"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Dups-ok-acknowledge") })
public class MessageHandler implements MessageListener {

    private static final Logger LOG = LoggerFactory.getLogger(MessageHandler.class);

    private WebSocketService ws;

    @Inject
    public void setWebSocketService(WebSocketService service) {
        this.ws = service;
    }

    @Override
    public void onMessage(Message message) {
        try {
            LOG.debug("Received message " + message + " instance " + this);
            if (message instanceof ObjectMessage) {
                Object object = ((ObjectMessage) message).getObject();
                // ping message onto the websocket service
                ws.sendMessage((org.tiatus.service.Message)object);

                // check to see if we need to do something else -- on race close we should create result pdf
            }
        } catch (JMSException e) {
            LOG.warn("JMSException " + e);
        }
    }
}
