package org.tiatus.service;

import org.tiatus.entity.Race;

import java.util.List;

/**
 * Created by johnreynolds on 25/06/2016.
 */
public interface RaceService {
    /**
     * Get Race for a given id
     * @return Race or null
     */
    Race getRaceForId(Long id);

    /**
     * Add race
     * @param race to create
     * @return created race
     * @throws ServiceException on error
     */
    Race addRace(Race race) throws ServiceException;

    /**
     * Remove a race
     * @param race to remove
     * @throws ServiceException on error
     */
    void deleteRace(Race race) throws ServiceException;

    /**
     * Get races
     * @return list of races
     */
    List<Race> getRaces();
}
