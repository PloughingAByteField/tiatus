package org.tiatus.service;

import org.tiatus.entity.RacePositionTemplate;
import org.tiatus.entity.RacePositionTemplateEntry;

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
    RacePositionTemplate addRacePositionTemplate(RacePositionTemplate template, String sessionId) throws ServiceException;

    /**
     * Remove a template
     * @param template to remove
     * @throws ServiceException on error
     */
    void deleteRacePositionTemplate(RacePositionTemplate template, String sessionId) throws ServiceException;

    /**
     * Update template
     * @param template to update
     * @throws ServiceException on error
     */
    RacePositionTemplate updateRacePositionTemplate(RacePositionTemplate template, String sessionId) throws ServiceException;

    /**
     * Get races
     * @return list of templates
     */
    List<RacePositionTemplate> getRacePositionTemplates();

    /**
     * Get RacePositionTemplate for a given id
     * @return RacePositionTemplate or null
     */
    RacePositionTemplate getTemplateForId(Long id);

    RacePositionTemplateEntry addTemplateEntry(RacePositionTemplateEntry entry, String sessionId) throws ServiceException;
    
    void deleteTemplateEntry(RacePositionTemplateEntry entry, String sessionId) throws ServiceException;
    
    RacePositionTemplateEntry updateTemplateEntry(RacePositionTemplateEntry entry, String sessionId) throws ServiceException;
}
