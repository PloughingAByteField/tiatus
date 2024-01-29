package org.tiatus.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tiatus.entity.RacePositionTemplate;
import org.tiatus.entity.RacePositionTemplateEntry;

import java.util.List;

/**
 * Created by johnreynolds on 09/03/2017.
 */
@Service
public class RacePositionTemplateDaoImpl implements RacePositionTemplateDao {

    private static final Logger LOG = LoggerFactory.getLogger(RacePositionTemplateDaoImpl.class);

    @Autowired
    private RacePositionTemplateRepository repository;

    @Autowired
    private RacePositionTemplateEntryRepository racePositionTemplateEntryRepository;

    @Override
    public RacePositionTemplate addRacePositionTemplate(RacePositionTemplate template) throws DaoException {
        LOG.debug("Adding RacePositionTemplate " + template.getName());
        try {
            if (!repository.existsById(template.getId())) {
                return repository.save(template);

            } else {
                String message = "Failed to add RacePositionTemplate due to existing RacePositionTemplate with same id " + template.getId();
                LOG.warn(message);
                throw new DaoException(message);
            }

        } catch (Exception e) {
            LOG.warn("Failed to persist RacePositionTemplate", e.getMessage());
            throw new DaoException(e);
        }
    }

    @Override
    public void deleteRacePositionTemplate(RacePositionTemplate template) throws DaoException {
        try {
            if (repository.existsById(template.getId())) {
                // remove the template entries
                List<RacePositionTemplateEntry> entries = racePositionTemplateEntryRepository.findByTemplate(template);
                for (int i = entries.size() - 1; i >= 0; i--) {
                    RacePositionTemplateEntry entry = entries.get(i);
                    racePositionTemplateEntryRepository.delete(entry);
                }
                repository.delete(template);

            } else {
                LOG.warn("No such RacePositionTemplate of id " + template.getId());
            }

        } catch (Exception e) {
            LOG.warn("Failed to delete RacePositionTemplate", e);
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public RacePositionTemplate updateRacePositionTemplate(RacePositionTemplate template) throws DaoException {
        try {
            return repository.save(template);

        } catch(Exception e) {
            LOG.warn("Failed to update RacePositionTemplate", e);
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public List<RacePositionTemplate> getRacePositionTemplates() {
        return repository.findByOrderByRace();
    }

    @Override
    public RacePositionTemplate getTemplateForId(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public RacePositionTemplateEntry addTemplateEntry(RacePositionTemplateEntry entry) throws DaoException {
        try {
            if (racePositionTemplateEntryRepository.findByTemplateAndPosition(entry.getTemplate(), entry.getPosition()) == null) {
                return racePositionTemplateEntryRepository.save(entry);

            } else {
                String message = "Failed to add template entry due to existing template entry with same template id " + entry.getTemplate().getId() + " and position id " + entry.getPosition().getId();
                LOG.warn(message);
                throw new DaoException(message);
            }

        } catch (Exception e) {
            LOG.warn("Failed to add template entry", e);
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public void deleteTemplateEntry(RacePositionTemplateEntry entry) throws DaoException {
        try {
            RacePositionTemplateEntry existing = racePositionTemplateEntryRepository.findByTemplateAndPosition(entry.getTemplate(), entry.getPosition());
            if (existing != null) {
                racePositionTemplateEntryRepository.delete(existing);

            } else {
                LOG.warn("No such template entry of template id " + entry.getTemplate().getId() + " and position id " + entry.getPosition().getId());
            }
        } catch (Exception e) {
            LOG.warn("Failed to delete template entry", e);
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public RacePositionTemplateEntry updateTemplateEntry(RacePositionTemplateEntry entry) throws DaoException {
        try {
            return racePositionTemplateEntryRepository.save(entry);

        } catch(Exception e) {
            LOG.warn("Failed to update template entry", e);
            throw new DaoException(e.getMessage());
        }
    }
}
