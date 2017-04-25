package org.tiatus.server.websocket;

/**
 * Created by johnreynolds on 25/04/2017.
 */
public class ClientDetails {
    private String userName;
    private String role;
    private String position;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
