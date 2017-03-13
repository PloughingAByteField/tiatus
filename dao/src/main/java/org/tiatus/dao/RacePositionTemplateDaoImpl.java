package org.tiatus.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.entity.RacePositionTemplate;
import org.tiatus.entity.RacePositionTemplateEntry;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.*;
import java.util.List;

/**
 * Created by johnreynolds on 09/03/2017.
 */
public class RacePositionTemplateDaoImpl implements RacePositionTemplateDao {

    private static final Logger LOG = LoggerFactory.getLogger(RacePositionTemplateDaoImpl.class);

    @PersistenceContext(unitName = "primary")
    protected EntityManager em;

    @Resource
    protected UserTransaction tx;

    @Override
    public RacePositionTemplate addRacePositionTemplate(RacePositionTemplate template) throws DaoException {
        LOG.debug("Adding RacePositionTemplate " + template.getName());
        try {
            RacePositionTemplate existing = null;
            if (template.getId() != null) {
                existing = em.find(RacePositionTemplate.class, template.getId());
            }
            if (existing == null) {
                tx.begin();
                RacePositionTemplate merged = em.merge(template);
                tx.commit();

                return merged;
            } else {
                String message = "Failed to add RacePositionTemplate due to existing RacePositionTemplate with same id " + template.getId();
                LOG.warn(message);
                throw new DaoException(message);
            }
        } catch (Exception e) {
            LOG.warn("Failed to persist RacePositionTemplate", e.getMessage());
            try { tx.rollback(); } catch (SystemException se) { LOG.warn("Failed to rollback", se); }
            throw new DaoException(e);
        }
    }

    @Override
    public void deleteRacePositionTemplate(RacePositionTemplate template) throws DaoException {
        try {
            RacePositionTemplate existing = null;
            if (template.getId() != null) {
                existing = em.find(RacePositionTemplate.class, template.getId());
            }
            if (existing != null) {
                tx.begin();
                // remove the template entries
                List<RacePositionTemplateEntry> entries = em.createQuery("FROM RacePositionTemplateEntry where template_id = :template").setParameter("template", template.getId()).getResultList();
                for (int i = entries.size() - 1; i >= 0; i--) {
                    RacePositionTemplateEntry entry = entries.get(i);
                    em.remove(em.contains(entry) ? entry : em.merge(entry));
                }
                em.remove(em.contains(template) ? template : em.merge(template));
                tx.commit();
            } else {
                LOG.warn("No such RacePositionTemplate of id " + template.getId());
            }
        } catch (Exception e) {
            LOG.warn("Failed to delete RacePositionTemplate", e);
            try { tx.rollback(); } catch (SystemException se) { LOG.warn("Failed to rollback", se); }
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public void updateRacePositionTemplate(RacePositionTemplate template) throws DaoException {
        try {
            tx.begin();
            em.merge(template);
            tx.commit();
        } catch(Exception e) {
            LOG.warn("Failed to update RacePositionTemplate", e);
            try { tx.rollback(); } catch (SystemException se) { LOG.warn("Failed to rollback", se); }
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public List<RacePositionTemplate> getRacePositionTemplates() {
        TypedQuery<RacePositionTemplate> query = em.createQuery("FROM RacePositionTemplate order by race", RacePositionTemplate.class);
        return query.getResultList();
    }

    @Override
    public RacePositionTemplate getTemplateForId(Long id) {
        return em.find(RacePositionTemplate.class, id);
    }

    @Override
    public RacePositionTemplateEntry addTemplateEntry(RacePositionTemplateEntry entry) throws DaoException {
        try {
            TypedQuery<RacePositionTemplateEntry> query = em.createQuery("FROM RacePositionTemplateEntry where template = :template and position = :position", RacePositionTemplateEntry.class);
            List<RacePositionTemplateEntry> existing = query.setParameter("template", entry.getTemplate()).setParameter("position", entry.getPosition()).getResultList();
            if (existing.isEmpty()) {
                tx.begin();
                RacePositionTemplateEntry merged = em.merge(entry);
                tx.commit();

                return merged;

            } else {
                String message = "Failed to add template entry due to existing template entry with same template id " + entry.getTemplate().getId() + " and position id " + entry.getPosition().getId();
                LOG.warn(message);
                throw new DaoException(message);
            }
        } catch (Exception e) {
            LOG.warn("Failed to add template entry", e);
            try { tx.rollback(); } catch (SystemException se) { LOG.warn("Failed to rollback", se); }
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public void deleteTemplateEntry(RacePositionTemplateEntry entry) throws DaoException {
        try {
            TypedQuery<RacePositionTemplateEntry> query = em.createQuery("FROM RacePositionTemplateEntry where template = :template and position = :position", RacePositionTemplateEntry.class);
            RacePositionTemplateEntry existing = query.setParameter("template", entry.getTemplate()).setParameter("position", entry.getPosition()).getSingleResult();
            if (existing != null) {
                tx.begin();
                em.remove(em.contains(entry) ? entry : em.merge(entry));
                tx.commit();
            } else {
                LOG.warn("No such template entry of template id " + entry.getTemplate().getId() + " and position id " + entry.getPosition().getId());
            }
        } catch (Exception e) {
            LOG.warn("Failed to delete template entry", e);
            try { tx.rollback(); } catch (SystemException se) { LOG.warn("Failed to rollback", se); }
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public void updateTemplateEntry(RacePositionTemplateEntry entry) throws DaoException {
        try {
            tx.begin();
            em.merge(entry);
            tx.commit();
        } catch(Exception e) {
            LOG.warn("Failed to update template entry", e);
            try { tx.rollback(); } catch (SystemException se) { LOG.warn("Failed to rollback", se); }
            throw new DaoException(e.getMessage());
        }
    }
}
