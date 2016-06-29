package org.tiatus.service;

import org.tiatus.entity.Race;

/**
 * Created by johnreynolds on 25/06/2016.
 */
public interface RaceService {
    Race addRace(Race race) throws ServiceException;
    void deleteRace(Race race) throws ServiceException;
}
