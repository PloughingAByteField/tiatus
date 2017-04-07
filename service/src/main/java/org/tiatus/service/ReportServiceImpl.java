package org.tiatus.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.entity.Race;

/**
 * Created by johnreynolds on 07/04/2017.
 */
public class ReportServiceImpl implements ReportService {

    private static final Logger LOG = LoggerFactory.getLogger(ReportServiceImpl.class);

    @Override
    public void createReportForRace(Race race) {
        LOG.debug("Got report creation request for race " + race.getName());
    }
}
