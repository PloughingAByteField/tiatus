package org.tiatus.service;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

/**
 * Created by johnreynolds on 06/04/2017.
 */
public class Message implements Serializable {
    private MessageType type;
    private String sessionId;
    private Object data;
    private String objectType;

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    @JsonIgnore
    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
        this.objectType = data.getClass().getSimpleName();
    }

    public String getObjectType() {
        return objectType;
    }
}
