package org.tiatus.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.entity.RacePositionTemplate;

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
        } catch (NotSupportedException | SystemException | HeuristicMixedException | HeuristicRollbackException | RollbackException e) {
            LOG.warn("Failed to persist RacePositionTemplate", e.getMessage());
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
                em.remove(em.contains(template) ? template : em.merge(template));
                tx.commit();
            } else {
                LOG.warn("No such RacePositionTemplate of id " + template.getId());
            }
        } catch (NotSupportedException | SystemException | HeuristicMixedException | HeuristicRollbackException | RollbackException e) {
            LOG.warn("Failed to delete RacePositionTemplate", e);
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public void updateRacePositionTemplate(RacePositionTemplate template) throws DaoException {
        try {
            tx.begin();
            em.merge(template);
            tx.commit();
        } catch (NotSupportedException | SystemException | HeuristicMixedException | HeuristicRollbackException | RollbackException e) {
            LOG.warn("Failed to update RacePositionTemplate", e);
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public List<RacePositionTemplate> getRacePositionTemplates() {
        TypedQuery<RacePositionTemplate> query = em.createQuery("FROM RacePositionTemplate order by race", RacePositionTemplate.class);
        List<RacePositionTemplate> results = query.getResultList();
        return results;
//        return query.getResultList();
    }
}
