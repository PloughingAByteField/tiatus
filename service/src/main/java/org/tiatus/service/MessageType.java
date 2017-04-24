package org.tiatus.service;

import java.io.Serializable;

/**
 * Created by johnreynolds on 06/04/2017.
 */
public enum MessageType implements Serializable {
    NOT_USED, ADD, DELETE, UPDATE, CONNECTED, DISCONNECTED;
}
