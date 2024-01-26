package org.tiatus.dao;

import org.tiatus.entity.Disqualification;

import java.util.List;

/**
 * Created by johnreynolds on 19/06/2016.
 */
public interface DisqualificationDao {
    /**
     * Get disqualifications
     * @return a list of disqualifications
     */
    List<Disqualification> getDisqualifications();

    /**
     * Add a new Disqualification
     * @param disqualification Disqualification to add
     * @return Disqualification Added disqualification
     * @throws DaoException on error
     */
    Disqualification addDisqualification(Disqualification disqualification) throws DaoException;

    /**
     * Remove a Disqualification
     * @param disqualification Disqualification to remove
     * @throws DaoException on error
     */
    void removeDisqualification(Disqualification disqualification) throws DaoException;

    /**
     * Update a Disqualification
     * @param disqualification Disqualification to update
     * @throws DaoException on error
     */
    Disqualification updateDisqualification(Disqualification disqualification) throws DaoException;

    Disqualification getDisqualificationForId(Long id);
}
