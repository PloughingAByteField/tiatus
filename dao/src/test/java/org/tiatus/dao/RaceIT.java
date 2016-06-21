package org.tiatus.dao;

import org.junit.Test;

import java.util.List;

/**
 * Created by johnreynolds on 19/06/2016.
 */
public class RaceIT {

    @Test
    public void getRaces() {
        Race race = new RaceImpl();
        List<org.tiatus.entity.Race> races = race.getRaces();
    }

}
