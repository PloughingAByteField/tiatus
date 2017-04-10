package org.tiatus.server.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.auth.TiatusSecurityContext;
import org.tiatus.auth.UserPrincipal;
import org.tiatus.entity.*;
import org.tiatus.role.Role;
import org.tiatus.service.Message;
import org.tiatus.service.WebSocketService;

import javax.enterprise.context.ApplicationScoped;
import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by johnreynolds on 06/04/2017.
 */
@ApplicationScoped
@ServerEndpoint(value = "/ws", configurator = HttpSessionConfigurator.class)
public class WebSocketServiceImpl implements WebSocketService {
    private static final Logger LOG = LoggerFactory.getLogger(WebSocketServiceImpl.class);

    private static ConcurrentHashMap<Session, HttpSession> clients = new ConcurrentHashMap<>();

    @OnOpen
    public void open(Session session, EndpointConfig config) {
        LOG.debug("Socket open " + session.getId());
        HttpSession httpSession = (HttpSession)config.getUserProperties().get(HttpSession.class.getName());
        LOG.debug("http session " + httpSession.getId());

        // are we an authenticated session
        UserPrincipal userPrincipal = (UserPrincipal)httpSession.getAttribute("principal");
        if (TiatusSecurityContext.isUserInRole(userPrincipal, Role.ADMIN)
            || TiatusSecurityContext.isUserInRole(userPrincipal, Role.ADJUDICATOR)
            || TiatusSecurityContext.isUserInRole(userPrincipal, Role.TIMING)) {
            clients.put(session, httpSession);
            LOG.debug("After add Have " + clients.size() + " clients for " + this);
        } else {
            LOG.warn("Got non logged in websocket attempt");
            close(session);
        }
    }

    @OnClose
    public void close(Session session) {
        LOG.debug("Socket close");
        try {
            session.close();
        } catch (IOException e) {
            LOG.warn("Failed to close socket", e);
        }
        removeSession(session);
        LOG.debug("After error Have " + clients.size() + " clients for " + this);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        LOG.warn("Socket error", error);
        removeSession(session);
        LOG.debug("After error Have " + clients.size() + " clients for " + this);
    }

    @OnMessage
    public void handleMessage(String data, Session session) {
        LOG.debug("Socket message " + data);
        try {
            session.getBasicRemote().sendText(data);
        } catch (IOException e) {
            LOG.warn("Failed to send message ", e);
        }
    }

    private void removeSession(Session session) {
        clients.remove(session);
    }

    @Override
    public void sendMessage(Message message) {
        LOG.debug("Send message");
        for (Session session: clients.keySet()) {
            HttpSession httpSession = clients.get(session);
            if (! message.getSessionId().equals(httpSession.getId())) {
                try {
                    if (shouldSendMessageToClient(message, httpSession)) {
                        session.getBasicRemote().sendText(convertToJson(message));
                    }
                } catch (IOException e) {
                    LOG.warn("Failed to send message to client", e);
                }
            }
        }
    }

    private boolean shouldSendMessageToClient(Message message, HttpSession httpSession) {
        if (message.getData() instanceof Race
                || message.getData() instanceof Position
                || message.getData() instanceof Club
                || message.getData() instanceof Event
                || message.getData() instanceof Entry) {
            return true;
        }

        UserPrincipal userPrincipal = (UserPrincipal)httpSession.getAttribute("principal");
        if (TiatusSecurityContext.isUserInRole(userPrincipal, Role.ADMIN)) {
            if (message.getData() instanceof User) {
                return true;
            }
        }

        if (TiatusSecurityContext.isUserInRole(userPrincipal, Role.ADJUDICATOR)) {
            if (message.getData() instanceof Disqualification || message.getData() instanceof Penalty) {
                return true;
            }
        }

        return false;
    }

    private String convertToJson(Message message) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(message);

        } catch (JsonProcessingException e) {
            LOG.warn("Failed to convert to json", e);
        }

        return null;
    }
}
