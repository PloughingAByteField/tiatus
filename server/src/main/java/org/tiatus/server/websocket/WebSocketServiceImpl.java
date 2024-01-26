package org.tiatus.server.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
// import org.tiatus.auth.TiatusSecurityContext;
// import org.tiatus.auth.UserPrincipal;
// import org.tiatus.entity.*;
// import org.tiatus.role.Role;
// import org.tiatus.service.Message;
// import org.tiatus.service.MessageType;
// import org.tiatus.service.WebSocketService;
import org.springframework.stereotype.Service;
import org.tiatus.service.Message;
import org.tiatus.service.WebSocketService;

// import javax.enterprise.context.ApplicationScoped;
// import javax.servlet.http.HttpSession;
// import javax.websocket.*;
// import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by johnreynolds on 06/04/2017.
 */
// @ApplicationScoped
// @ServerEndpoint(value = "/ws", configurator = HttpSessionConfigurator.class)
@Service
public class WebSocketServiceImpl implements WebSocketService {

    @Override
    public void sendMessage(Message message) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'sendMessage'");
    }
    // private static final Logger LOG = LoggerFactory.getLogger(WebSocketServiceImpl.class);

    // private static ConcurrentHashMap<Session, HttpSession> clients = new ConcurrentHashMap<>();
    // private static ConcurrentHashMap<Session, ClientDetails> connectedDetails = new ConcurrentHashMap<>();

    // @OnOpen
    // public void open(Session session, EndpointConfig config) {
    //     LOG.debug("Socket open " + session.getId());
    //     boolean loggedIn = false;
    //     HttpSession httpSession = (HttpSession)config.getUserProperties().get(HttpSession.class.getName());
    //     if (httpSession != null) {
    //         LOG.debug("http session " + httpSession.getId());
    //         // are we an authenticated session
    //         UserPrincipal userPrincipal = (UserPrincipal) httpSession.getAttribute("principal");
    //         if (TiatusSecurityContext.isUserInRole(userPrincipal, Role.ADMIN)
    //                 || TiatusSecurityContext.isUserInRole(userPrincipal, Role.ADJUDICATOR)
    //                 || TiatusSecurityContext.isUserInRole(userPrincipal, Role.TIMING)) {
    //             loggedIn = true;
    //             clients.put(session, httpSession);
    //             ClientDetails details = new ClientDetails();
    //             details.setUserName(userPrincipal.getName());
    //             details.setRole(getUserRole(userPrincipal));
    //             connectedDetails.put(session, details);
    //             LOG.debug("After add Have " + clients.size() + " clients for " + this);

    //             // send connected out
    //             sendMessage(Message.createMessage(buildContent(details), MessageType.CONNECTED, httpSession.getId()));
    //             // send who else is connected and position to the connecting client
    //             sendConnectedClients(session);
    //         }
    //     }
    //     if (!loggedIn) {
    //         LOG.warn("Got non logged in websocket attempt");
    //         close(session);
    //     }
    // }

    // @OnClose
    // public void close(Session session) {
    //     LOG.debug("Socket close");
    //     removeSession(session);
    //     LOG.debug("After close Have " + clients.size() + " clients for " + this);
    // }

    // @OnError
    // public void onError(Session session, Throwable error) {
    //     LOG.warn("Socket error", error);
    //     removeSession(session);
    //     LOG.debug("After error Have " + clients.size() + " clients for " + this);
    // }

    // @OnMessage
    // public void handleMessage(String data, Session session) {
    //     LOG.debug("Socket message " + data);
    //     HttpSession httpSession = clients.get(session);
    //     Object jsonObject = getJsonObject(data);
    //     if (jsonObject instanceof Message) {
    //         Message message = (Message)jsonObject;
    //         ClientDetails details = connectedDetails.get(session);
    //         if (message.getType().equals(MessageType.CONNECTED) && message.getData() instanceof Position) {
    //             Position position = (Position) message.getData();
    //             if (position != null) {
    //                 details.setPosition(position.getName());
    //             }
    //             sendMessage(Message.createMessage(buildContent(details), MessageType.CONNECTED, httpSession.getId()));
    //         } else if (message.getType().equals(MessageType.CHAT)) {
    //             ConverstationMessage mess = (ConverstationMessage) message.getData();
    //             String from = details.getUserName() + "/" + details.getRole();
    //             if (details.getPosition() != null) {
    //                 from = from + "/" + details.getPosition();
    //             }
    //             mess.setFrom(from);

    //             if (mess.getTo().equals("ALL")) {
    //                 sendMessage(Message.createMessage(message.getData(), MessageType.CHAT, httpSession.getId()));
    //             } else {
    //                 Session to = getSessionForMessageTo(mess.getTo());
    //                 if (to != null) {
    //                     try {
    //                         to.getBasicRemote().sendText(convertToJson(message));
    //                     } catch (IOException e) {
    //                         LOG.warn("Failed to send message to client", e);
    //                     }
    //                 }
    //             }
    //         }
    //     }
    // }

    // private String getUserRole(UserPrincipal userPrincipal) {
    //     if (TiatusSecurityContext.isUserInRole(userPrincipal, Role.ADMIN)) {
    //         return Role.ADMIN;

    //     } else if (TiatusSecurityContext.isUserInRole(userPrincipal, Role.ADJUDICATOR)) {
    //         return Role.ADJUDICATOR;

    //     } else if (TiatusSecurityContext.isUserInRole(userPrincipal, Role.TIMING)) {
    //         return Role.TIMING;
    //     }
    //     return null;
    // }

    // private void removeSession(Session session) {
    //     HttpSession httpSession = clients.get(session);
    //     ClientDetails details = connectedDetails.get(session);
    //     if (details != null) {
    //         sendMessage(Message.createMessage(buildContent(details), MessageType.DISCONNECTED, httpSession.getId()));
    //     }
    //     clients.remove(session);
    //     connectedDetails.remove(session);
    //     try {
    //         session.close();
    //     } catch (IOException e) {
    //         LOG.warn("Failed to close socket", e);
    //     }
    // }

    // @Override
    // public void sendMessage(Message message) {
    //     LOG.debug("Send message");
    //     for (Session session: clients.keySet()) {
    //         HttpSession httpSession = clients.get(session);
    //         if (! message.getSessionId().equals(httpSession.getId())) {
    //             try {
    //                 if (shouldSendMessageToClient(message, session)) {
    //                     session.getBasicRemote().sendText(convertToJson(message));
    //                 }
    //             } catch (IOException e) {
    //                 LOG.warn("Failed to send message to client", e);
    //             }
    //         }
    //     }
    // }

    // private Session getSessionForMessageTo(String to){
    //     for (Session session: connectedDetails.keySet()) {
    //         ClientDetails details = connectedDetails.get(session);
    //         String client[] = to.split("/");
    //         if (details.getPosition() != null && client.length != 3) {
    //             continue;
    //         }
    //         if (client[0].equals(details.getUserName()) && client[1].equals(details.getRole())) {
    //             if (details.getPosition() != null) {
    //                 if (client[2].equals(details.getPosition())) {
    //                     return session;
    //                 } else {
    //                     continue;
    //                 }
    //             } else {
    //                 return session;
    //             }
    //         }
    //     }

    //     return null;
    // }

    // private String buildContent(ClientDetails details) {
    //     StringBuffer content = new StringBuffer("{ \"userName\": \"" + details.getUserName() + "\", \"role\": \"" + details.getRole() + "\"");
    //     if (details.getPosition() != null) {
    //         content.append(", \"position\": \"" + details.getPosition() + "\"");
    //     }
    //     content.append("}");
    //     return content.toString();
    // }

    // private void sendConnectedClients(Session session) {
    //     for (Session client: clients.keySet()) {
    //         if (! session.getId().equals(client.getId())) {
    //             HttpSession httpSession = clients.get(client);
    //             try {
    //                 ClientDetails details = connectedDetails.get(client);
    //                 Message message = Message.createMessage(buildContent(details), MessageType.CONNECTED, null);

    //                 session.getBasicRemote().sendText(convertToJson(message));
    //             } catch (IOException e) {
    //                 LOG.warn("Failed to send message to client", e);
    //             }
    //         }
    //     }
    // }

    // private boolean shouldSendMessageToClient(Message message, Session session) {
    //     if (message.getData() instanceof String
    //             || message.getData() instanceof ConverstationMessage
    //             || message.getData() instanceof Race
    //             || message.getData() instanceof Position
    //             || message.getData() instanceof Club
    //             || message.getData() instanceof Event
    //             || message.getData() instanceof Entry) {
    //         return true;
    //     }

    //     try {
    //         HttpSession httpSession = clients.get(session);
    //         UserPrincipal userPrincipal = (UserPrincipal) httpSession.getAttribute("principal");
    //         if (TiatusSecurityContext.isUserInRole(userPrincipal, Role.ADMIN)) {
    //             if (message.getData() instanceof User
    //                     || message.getData() instanceof RacePositionTemplate
    //                     || message.getData() instanceof RacePositionTemplateEntry) {
    //                 return true;
    //             }
    //         }

    //         if (TiatusSecurityContext.isUserInRole(userPrincipal, Role.ADJUDICATOR)) {
    //             if (message.getData() instanceof Disqualification || message.getData() instanceof Penalty) {
    //                 return true;
    //             }
    //         }
    //     } catch (IllegalStateException e) {
    //         LOG.warn("Session is no longer valid");
    //         removeSession(session);
    //     }
    //     return false;
    // }

    // private String convertToJson(Message message) {
    //     try {
    //         ObjectMapper mapper = new ObjectMapper();
    //         return mapper.writeValueAsString(message);

    //     } catch (JsonProcessingException e) {
    //         LOG.warn("Failed to convert to json", e);
    //     }

    //     return null;
    // }

    // private Object getJsonObject(String json) {
    //     ObjectMapper mapper = new ObjectMapper();
    //     try {
    //         JsonNode rootNode = mapper.readValue(json, JsonNode.class);
    //         if (rootNode.get("type") != null && rootNode.get("data") != null) {
    //             Object data = null;
    //             if (rootNode.get("objectType") != null) {
    //                 if (rootNode.get("objectType").asText().equals("Position")) {
    //                     data = mapper.readValue(rootNode.get("data").asText(), Position.class);
    //                 }
    //                 if (rootNode.get("objectType").asText().equals("ConverstationMessage")) {
    //                     data = mapper.readValue(rootNode.get("data").asText(), ConverstationMessage.class);
    //                 }
    //             }
    //             Object type = mapper.treeToValue(rootNode.get("type"), MessageType.class);
    //             if (data != null && type != null) {
    //                 Message message = new Message();
    //                 message.setData(data);
    //                 message.setType((MessageType) type);
    //                 return message;
    //             }
    //         }
    //     } catch (IOException e) {
    //         LOG.warn("Failed to convert to json", e);
    //     }

    //     return null;
    // }
}
