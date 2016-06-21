package org.tiatus.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by johnreynolds on 19/06/2016.
 */
@Entity
@Table(name = "race")
public class Race {

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
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "UseExistingOrGenerateIdGenerator")
    @GenericGenerator(name="UseExistingOrGenerateIdGenerator",
            strategy="org.tiatus.entity.UseExistingOrGenerateIdGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence", value = "race_id_sequence")
    })
    @Column(name = "id")
    private Long id;

    @PrePersist
    public void setupDefaults() {
        closed = false;
        active = false;
        drawLocked = false;
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
        if (this == o) return true;
        if (!(o instanceof Race)) return false;

        Race race = (Race) o;

        if (active != race.active) return false;
        if (closed != race.closed) return false;
        if (raceOrder != race.raceOrder) return false;
        if (id != null ? !id.equals(race.id) : race.id != null) return false;
        if (name != null ? !name.equals(race.name) : race.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (active ? 1 : 0);
        result = 31 * result + (closed ? 1 : 0);
        result = 31 * result + raceOrder;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        return result;
    }
}
