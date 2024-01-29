package org.tiatus.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * Created by johnreynolds on 15/02/2015.
 */
@Entity
@Table(name = "race_event", uniqueConstraints = { @UniqueConstraint(columnNames = { "race_id", "event_id" }) })
public class RaceEvent implements Serializable {

    private static final long serialVersionUID = 486591711029352049L;

    @Id
    @SequenceGenerator(name="race_event_id_sequence",
                       sequenceName="race_event_id_sequence",
                       allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "race_event_id_sequence")
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="RACE_ID", nullable=false, updatable=false)
    private Race race;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="EVENT_ID", nullable=false, updatable=false)
    private Event event;

    @Column(name = "race_event_order")
    private int raceEventOrder;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonGetter("race")
    public Long getRaceId() {
        return race.getId();
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    @JsonGetter("event")
    public Long getEventId() {
        return event.getId();
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public int getRaceEventOrder() {
        return raceEventOrder;
    }

    public void setRaceEventOrder(int raceEventOrder) {
        this.raceEventOrder = raceEventOrder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof RaceEvent)) {
            return false;
        }

        RaceEvent raceEvent = (RaceEvent) o;

        return new EqualsBuilder()
            .append(this.event, raceEvent.event)
            .append(this.race, raceEvent.race)
            .append(this.raceEventOrder, raceEvent.raceEventOrder)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(this.event)
            .append(this.race)
            .append(this.raceEventOrder)
            .toHashCode();
    }
}
