package org.tiatus.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by johnreynolds on 19/06/2016.
 */
public class RaceImpl implements Race {

    @PersistenceContext(unitName = "primary")
    protected EntityManager em;

    public List<org.tiatus.entity.Race> getRaces() {
//        Query query = em.createQuery("FROM Race order by raceOrder");
//        return query.getResultList();
        return null;
    }
}
