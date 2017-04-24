package org.tiatus.server.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.auth.TiatusSecurityContext;
import org.tiatus.auth.UserPrincipal;
import org.tiatus.entity.*;
import org.tiatus.role.Role;
import org.tiatus.service.Message;
import org.tiatus.service.MessageType;
import org.tiatus.service.WebSocketService;

import javax.enterprise.context.ApplicationScoped;
import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

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

            // send connected out
            sendMessage(Message.createMessage("userName: " + userPrincipal.getName(), MessageType.CONNECTED, httpSession.getId()));

        } else {
            LOG.warn("Got non logged in websocket attempt");
            close(session);
        }
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
        HttpSession httpSession = clients.get(session);
        UserPrincipal userPrincipal = (UserPrincipal)httpSession.getAttribute("principal");
        Object jsonObject = getJsonObject(data);
        if (jsonObject instanceof Message) {
            Message message = (Message)jsonObject;
            if (message.getType().equals(MessageType.CONNECTED) && message.getData() instanceof Position) {
                Position position = (Position) message.getData();
                sendMessage(Message.createMessage("userName: " + userPrincipal.getName() + ", Position: " + position.getName(), MessageType.CONNECTED, httpSession.getId()));

//                } else if (message.getAction().equals(MessageType.INFO) || message.getAction().equals(MessageType.ALERT)) {
//                    sendChatMessage(message);
            }
        }
    }

    private void removeSession(Session session) {
        // send disconnected out
        HttpSession httpSession = clients.get(session);
        try {
            UserPrincipal userPrincipal = (UserPrincipal)httpSession.getAttribute("principal");
            sendMessage(Message.createMessage(userPrincipal.getName(), MessageType.DISCONNECTED, httpSession.getId()));
        } catch (IllegalStateException e) {
            LOG.debug("skipping sending disconnect to invalid session");
        }

        clients.remove(session);
        try {
            session.close();
        } catch (IOException e) {
            LOG.warn("Failed to close socket", e);
        }
    }

    @Override
    public void sendMessage(Message message) {
        LOG.debug("Send message");
        for (Session session: clients.keySet()) {
            HttpSession httpSession = clients.get(session);
            if (! message.getSessionId().equals(httpSession.getId())) {
                try {
                    if (shouldSendMessageToClient(message, session)) {
                        session.getBasicRemote().sendText(convertToJson(message));
                    }
                } catch (IOException e) {
                    LOG.warn("Failed to send message to client", e);
                }
            }
        }
    }

    private boolean shouldSendMessageToClient(Message message, Session session) {
        if (message.getData() instanceof String
                || message.getData() instanceof Race
                || message.getData() instanceof Position
                || message.getData() instanceof Club
                || message.getData() instanceof Event
                || message.getData() instanceof Entry) {
            return true;
        }

        try {
            HttpSession httpSession = clients.get(session);
            UserPrincipal userPrincipal = (UserPrincipal) httpSession.getAttribute("principal");
            if (TiatusSecurityContext.isUserInRole(userPrincipal, Role.ADMIN)) {
                if (message.getData() instanceof User
                        || message.getData() instanceof RacePositionTemplate
                        || message.getData() instanceof RacePositionTemplateEntry) {
                    return true;
                }
            }

            if (TiatusSecurityContext.isUserInRole(userPrincipal, Role.ADJUDICATOR)) {
                if (message.getData() instanceof Disqualification || message.getData() instanceof Penalty) {
                    return true;
                }
            }
        } catch (IllegalStateException e) {
            LOG.warn("Session is no longer valid");
            removeSession(session);
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

    private Object getJsonObject(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode rootNode = mapper.readValue(json, JsonNode.class);
            if (rootNode.get("type") != null && rootNode.get("data") != null) {
                Object data = null;
                if (rootNode.get("objectType") != null) {
                    if (rootNode.get("objectType").asText().equals("Position")) {
                        data = mapper.readValue(rootNode.get("data").asText(), Position.class);
                    }
                }
                Object action = mapper.treeToValue(rootNode.get("type"), MessageType.class);
                if (data != null && action != null) {
                    Message message = new Message();
                    message.setData(data);
                    message.setType((MessageType) action);
                    return message;
                }
            }
        } catch (IOException e) {
            LOG.warn("Failed to convert to json", e);
        }

        return null;
    }
}
