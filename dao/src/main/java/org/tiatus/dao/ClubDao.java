package org.tiatus.dao;

import org.tiatus.entity.Club;

import java.util.List;

/**
 * Created by johnreynolds on 19/06/2016.
 */
public interface ClubDao {
    /**
     * Get Clubs
     * @return a list of Clubs
     */
    List<Club> getClubs();

    /**
     * Add a new Club
     * @param club Club to add
     * @return Club Added club
     * @throws DaoException on error
     */
    Club addClub(Club club) throws DaoException;

    /**
     * Remove a Club
     * @param club Club to remove
     * @throws DaoException on error
     */
    void removeClub(Club club) throws DaoException;

    /**
     * Update a Club
     * @param club Club to update
     * @return Club Updated club
     * @throws DaoException on error
     */
    Club updateClub(Club club) throws DaoException;

    Club getClubForId(Long id);
}
