package org.tiatus.dao;

import org.tiatus.entity.Penalty;

import java.util.List;

/**
 * Created by johnreynolds on 19/06/2016.
 */
public interface PenaltyDao {
    /**
     * Get Penalties
     * @return a list of Penalties 
     */
    List<Penalty> getPenalties();

    /**
     * Add a new Penalty
     * @param penalty Penalty to add
     * @return Penalty Added penalty
     * @throws DaoException on error
     */
    Penalty addPenalty(Penalty penalty) throws DaoException;

    /**
     * Remove a Penalty
     * @param penalty Penalty to remove
     * @throws DaoException on error
     */
    void removePenalty(Penalty penalty) throws DaoException;

    /**
     * Update a Penalty
     * @param penalty Penalty to update
     * @throws DaoException on error
     */
    void updatePenalty(Penalty penalty) throws DaoException;

}
