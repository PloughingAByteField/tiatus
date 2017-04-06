package org.tiatus.server.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.auth.UserPrincipal;
import org.tiatus.entity.User;
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

    private static ConcurrentLinkedQueue<Session> clients = new ConcurrentLinkedQueue<>();
    private static ConcurrentHashMap<Session, HttpSession> httpSessions = new ConcurrentHashMap<>();

    @OnOpen
    public void open(Session session, EndpointConfig config) {
        LOG.debug("Socket open " + session.getId());
        clients.add(session);
        HttpSession httpSession = (HttpSession)config.getUserProperties().get(HttpSession.class.getName());
        LOG.debug("http session " + httpSession.getId());
        httpSessions.put(session, httpSession);
        LOG.debug("After add Have " + clients.size() + " clients for " + this);
    }

    @OnClose
    public void close(Session session) {
        LOG.debug("Socket close");
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
        httpSessions.remove(session);
        clients.remove(session);
    }

    private User getUserForSession(Session session) {
        HttpSession httpSession = httpSessions.get(session);
        if (httpSession != null) {
            UserPrincipal p = (UserPrincipal)httpSession.getAttribute("principal");
            return p.getUser();
        }
        return null;
    }

    @Override
    public void sendMessage(Message message) {
        LOG.debug("Send message");
        for (Session session: clients) {
            try {
                session.getBasicRemote().sendText(convertToJson(message));
            } catch (IOException e) {
                LOG.warn("Failed to send message to client", e);
            }
        }
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
