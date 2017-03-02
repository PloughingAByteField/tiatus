package org.tiatus.service;

import org.tiatus.entity.Disqualification;

import java.util.List;

/**
 * Created by johnreynolds on 25/06/2016.
 */
public interface DisqualificationService {
    /**
     * Add disqualification
     * @param disqualification to create
     * @return created disqualification
     * @throws ServiceException on error
     */
    Disqualification addDisqualification(Disqualification disqualification) throws ServiceException;

    /**
     * Remove a disqualification
     * @param disqualification to remove
     * @throws ServiceException on error
     */
    void deleteDisqualification(Disqualification disqualification) throws ServiceException;

    /**
     * update disqualification
     * @param disqualification to update
     * @throws ServiceException on error
     */
    void updateDisqualification(Disqualification disqualification) throws ServiceException;

    /**
     * Get disqualifications
     * @return list of disqualifications
     */
    List<Disqualification> getDisqualifications();
}
