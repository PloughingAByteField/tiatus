package org.tiatus.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.entity.Disqualification;
import org.tiatus.entity.EntryPositionTime;
import org.tiatus.entity.Penalty;
import org.tiatus.entity.Race;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
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
@TransactionManagement(value= TransactionManagementType.BEAN)
public class MessageHandler implements MessageListener {

    private static final Logger LOG = LoggerFactory.getLogger(MessageHandler.class);

    private WebSocketService ws;
    private ReportService reportService;

    @Inject
    public void setWebSocketService(WebSocketService service) {
        this.ws = service;
    }

    @Inject
    public void setReportService(ReportService service) {
        this.reportService = service;
    }

    @Override
    public void onMessage(Message message) {
        try {
            LOG.debug("Received message " + message + " instance " + this);
            if (message instanceof ObjectMessage) {
                Object object = ((ObjectMessage) message).getObject();
                if (object instanceof org.tiatus.service.Message) {
                    processMessage((org.tiatus.service.Message) object);
                }
            }
        } catch (JMSException e) {
            LOG.warn("JMSException " + e);
        }
    }

    private void processMessage(org.tiatus.service.Message message) {
        // ping message onto the websocket service
        ws.sendMessage(message);

        Race race = null;
        if (message.getData() instanceof Race) {
            race = (Race)message.getData();

        } else if (message.getData() instanceof EntryPositionTime) {
            EntryPositionTime pt = (EntryPositionTime)message.getData();
            race = pt.getEntry().getRace();

        } else if (message.getData() instanceof Disqualification) {
            Disqualification disqualification = (Disqualification)message.getData();
            race = disqualification.getEntry().getRace();

        } else if (message.getData() instanceof Penalty) {
            Penalty penalty = (Penalty)message.getData();
            race = penalty.getEntry().getRace();
        }

        if (race != null) {
            if (race.isClosed()) {
                reportService.createReportForRace(race);
            }
        }
    }

}
