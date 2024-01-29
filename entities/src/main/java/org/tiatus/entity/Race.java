package org.tiatus.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * Created by johnreynolds on 19/06/2016.
 */
@Entity
@Table(name = "race")
public class Race implements Serializable {

    private static final long serialVersionUID = 5540680572499312363L;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "active")
    private boolean active;

    @Column(name = "closed")
    private boolean closed;

    @Column(name = "draw_locked")
    private boolean drawLocked = false;

    @Column(name = "race_order")
    private int raceOrder;

    @Column(name = "start_time")
    private String startTime;

    @Id
    @SequenceGenerator(name="race_id_sequence",
                       sequenceName="race_id_sequence",
                       allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "race_id_sequence")
    @Column(name = "id")
    private Long id;

    @PrePersist
    protected void setupDefaults() {
        closed = false;
        active = false;
        drawLocked = false;
    }

    public Race() {}

    public Race(Long id) {
        this.setId(id);
    }

    public boolean isDrawLocked() {
        return drawLocked;
    }

    public void setDrawLocked(boolean drawLocked) {
        this.drawLocked = drawLocked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public int getRaceOrder() {
        return raceOrder;
    }

    public void setRaceOrder(int raceOrder) {
        this.raceOrder = raceOrder;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Race)) {
            return false;
        }

        Race race = (Race) o;
        return new EqualsBuilder()
                .append(this.id, race.id)
                .append(this.name, race.name)
                .append(this.active, race.active)
                .append(this.closed, race.closed)
                .append(this.raceOrder, race.raceOrder)
                .append(this.startTime, race.startTime)
                .append(this.drawLocked, race.drawLocked)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(this.id)
                .append(this.name)
                .append(this.active)
                .append(this.closed)
                .append(this.raceOrder)
                .append(this.startTime)
                .append(this.drawLocked)
                .toHashCode();
    }
}
