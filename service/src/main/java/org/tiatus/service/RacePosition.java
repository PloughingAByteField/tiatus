package org.tiatus.service;

import org.tiatus.entity.Position;
import org.tiatus.entity.Race;

import java.io.Serializable;

public class RacePosition implements Serializable {
    private Position position;
    
    private Race race;

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((position == null) ? 0 : position.hashCode());
        result = prime * result + ((race == null) ? 0 : race.hashCode());
        return result;
    }

    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RacePosition other = (RacePosition) obj;
        if (position == null) {
            if (other.position != null)
                return false;
        } else if (!position.equals(other.position))
            return false;
        if (race == null) {
            if (other.race != null)
                return false;
        } else if (!race.equals(other.race))
            return false;
        return true;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
