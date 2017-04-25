package org.tiatus.server.websocket;

/**
 * Created by johnreynolds on 25/04/2017.
 */
public class ConverstationMessage {
    private String from;
    private String to;
    private String message;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
