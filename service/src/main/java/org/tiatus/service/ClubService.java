package org.tiatus.service;

import org.tiatus.entity.Club;

import java.util.List;

/**
 * Created by johnreynolds on 25/06/2016.
 */
public interface ClubService {
    /**
     * Add club
     * @param club to create
     * @return created club
     * @throws ServiceException on error
     */
    Club addClub(Club club) throws ServiceException;

    /**
     * Remove a club
     * @param club to remove
     * @throws ServiceException on error
     */
    void deleteClub(Club club) throws ServiceException;

    /**
     * update club
     * @param club to update
     * @throws ServiceException on error
     */
    void updateClub(Club club) throws ServiceException;

    /**
     * Get clubs
     * @return list of clubs
     */
    List<Club> getClubs();
}
