package org.tiatus.dao;

import org.tiatus.entity.Race;

import java.util.List;

/**
 * Created by johnreynolds on 19/06/2016.
 */
public interface RaceDao {
    List<Race> getRaces();
    void addRace(Race race);
    void removeRace(Race race);
    void updateRace(Race race);

}
