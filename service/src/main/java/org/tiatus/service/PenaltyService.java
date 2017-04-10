package org.tiatus.service;

import org.tiatus.entity.Penalty;

import java.util.List;

/**
 * Created by johnreynolds on 25/06/2016.
 */
public interface PenaltyService {
    /**
     * Add penalty
     * @param penalty to create
     * @return created penalty
     * @throws ServiceException on error
     */
    Penalty addPenalty(Penalty penalty, String sessionId) throws ServiceException;

    /**
     * Remove a penalty
     * @param penalty to remove
     * @throws ServiceException on error
     */
    void deletePenalty(Penalty penalty, String sessionId) throws ServiceException;

    /**
     * update penalty
     * @param penalty to update
     * @throws ServiceException on error
     */
    void updatePenalty(Penalty penalty, String sessionId) throws ServiceException;

    /**
     * Get penalties
     * @return list of penalties
     */
    List<Penalty> getPenalties();
}
