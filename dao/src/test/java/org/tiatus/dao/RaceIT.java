package org.tiatus.dao;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.util.List;

/**
 * Created by johnreynolds on 19/06/2016.
 */
public class RaceIT {

    private static final Logger LOG = LoggerFactory.getLogger(RaceIT.class);
    private RaceImpl race;
    private EntityManager em;

    @Before
    public void setUp() throws Exception {
        race = new RaceImpl();
        em = Persistence.createEntityManagerFactory("primary").createEntityManager();
        race.em = em;
    }

    @After
    public void tearDown() throws Exception {
        em.close();
    }

    @Test
    public void getRaces() {
        List<org.tiatus.entity.Race> races = race.getRaces();
        Assert.assertTrue(races.isEmpty());

        // add race
        em.getTransaction().begin();
        org.tiatus.entity.Race race1 = new org.tiatus.entity.Race();
        race1.setId(new Long(1));
        race1.setName("Race 1");
        org.tiatus.entity.Race race2 = new org.tiatus.entity.Race();
        race2.setId(new Long(2));
        race2.setName("Race 2");
        em.merge(race1);
        em.merge(race2);
        em.getTransaction().commit();


        races = race.getRaces();
        Assert.assertTrue(!races.isEmpty());
        Assert.assertTrue(races.size() == 2);
    }

}
