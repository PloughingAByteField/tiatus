package org.tiatus.server.websocket;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.tiatus.service.Message;

@Component
public class WebsocketHandlerText extends TextWebSocketHandler {

    private static final Logger LOG = LoggerFactory.getLogger(WebsocketHandlerText.class);

    private List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    public void sendMessage(Message message) throws IOException {
        LOG.debug("Have message to send");
        for (WebSocketSession webSocketSession : sessions) {
            LOG.debug("Sending message");
            webSocketSession.sendMessage(new TextMessage(message.toString()));
        }
    } 

	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message)
			throws InterruptedException, IOException {
                LOG.debug("Have message to send to handle");
        // ObjectMapper mapper = new ObjectMapper();

		for (WebSocketSession webSocketSession : sessions) {
            // Map value = mapper.readValue(message.getPayload())
			// Map value = new Gson().fromJson(message.getPayload(), Map.class);
			// webSocketSession.sendMessage(new TextMessage("Hello " + value.get("name") + " !"));
            LOG.debug("Sending message in handle");
            webSocketSession.sendMessage(message);
		}
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		//the messages will be broadcasted to all users.
		sessions.add(session);
	}
}
