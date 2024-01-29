package org.tiatus.dao;

import org.tiatus.entity.RacePositionTemplate;
import org.tiatus.entity.RacePositionTemplateEntry;

import java.util.List;

/**
 * Created by johnreynolds on 10/10/2016.
 */
public interface RacePositionTemplateDao {
    /**
     * Add a RaceEvent
     *
     * @param template RacePositionTemplate to create
     * @return created RacePositionTemplate
     * @throws DaoException on error
     */
    RacePositionTemplate addRacePositionTemplate(RacePositionTemplate template) throws DaoException;

    /**
     * Remove a RacePositionTemplate
     *
     * @param template RacePositionTemplate to remove
     * @throws DaoException on error
     */
    void deleteRacePositionTemplate(RacePositionTemplate template) throws DaoException;

    /**
     * Update a RacePositionTemplate
     * @param template RacePositionTemplate to update
     * @throws DaoException on error
     */
    RacePositionTemplate updateRacePositionTemplate(RacePositionTemplate template) throws DaoException;

    /**
     * Get list of RaceEvents
     *
     * @return list of race position templates
     */
    List<RacePositionTemplate> getRacePositionTemplates();

    /**
     * Get RacePositionTemplate for a given id
     * @return RacePositionTemplate or null
     */
    RacePositionTemplate getTemplateForId(Long id);

    RacePositionTemplateEntry addTemplateEntry(RacePositionTemplateEntry entry) throws DaoException;
    
    void deleteTemplateEntry(RacePositionTemplateEntry entry) throws DaoException;
    
    RacePositionTemplateEntry updateTemplateEntry(RacePositionTemplateEntry entry) throws DaoException;
}