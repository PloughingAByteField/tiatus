package org.tiatus.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.dao.RaceDao;
import org.tiatus.entity.Race;

import javax.enterprise.inject.Default;
import javax.inject.Inject;

/**
 * Created by johnreynolds on 25/06/2016.
 */
@Default
public class RaceServiceImpl implements RaceService {

    private static final Logger LOG = LoggerFactory.getLogger(RaceService.class);

    @Inject
    private RaceDao dao;

    public void addRace(Race race) {
        LOG.debug("Adding race " + race);
        dao.addRace(race);
    }
}
