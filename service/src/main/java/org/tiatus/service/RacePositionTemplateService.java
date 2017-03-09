package org.tiatus.service;

import org.tiatus.entity.RacePositionTemplate;

import java.util.List;

/**
 * Created by johnreynolds on 25/06/2016.
 */
public interface RacePositionTemplateService {

    /**
     * Add template
     * @param template to create
     * @return created template
     * @throws ServiceException on error
     */
    RacePositionTemplate addRacePositionTemplate(RacePositionTemplate template) throws ServiceException;

    /**
     * Remove a template
     * @param template to remove
     * @throws ServiceException on error
     */
    void deleteRacePositionTemplate(RacePositionTemplate template) throws ServiceException;

    /**
     * Update template
     * @param template to update
     * @throws ServiceException on error
     */
    void updateRacePositionTemplate(RacePositionTemplate template) throws ServiceException;

    /**
     * Get races
     * @return list of templates
     */
    List<RacePositionTemplate> getRacePositionTemplates();
}
