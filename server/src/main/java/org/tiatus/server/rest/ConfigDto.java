package org.tiatus.server.rest;

import java.io.Serializable;

public class ConfigDto implements Serializable {
    private String title;

    private String logo;
    
    private String footer;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }
}
