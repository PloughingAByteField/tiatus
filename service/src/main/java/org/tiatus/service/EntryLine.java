package org.tiatus.service;

import com.opencsv.bean.CsvBindByName;

public class EntryLine {
    @CsvBindByName(column = "Event")
    private String event;

    @CsvBindByName(column = "Race")
    private String race;
    
    @CsvBindByName(column = "Number")
    private Integer number;
    
    @CsvBindByName(column = "Club")
    private String club;
    
    @CsvBindByName(column = "Name")
    private String name;
    
    @CsvBindByName(column = "Weighting")
    private String weighting;
    
    @CsvBindByName(column = "Time Only")
    private Boolean timeOnly;

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getClub() {
        return club;
    }

    public void setClub(String club) {
        this.club = club;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWeighting() {
        return weighting;
    }

    public void setWeighting(String weighting) {
        this.weighting = weighting;
    }

    public Boolean getTimeOnly() {
        return timeOnly;
    }

    public void setTimeOnly(Boolean timeOnly) {
        this.timeOnly = timeOnly;
    }

    
}