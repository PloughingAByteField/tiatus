package org.tiatus.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.entity.EntryPositionTime;
import org.tiatus.entity.Position;
import org.tiatus.entity.Race;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnreynolds on 19/06/2016.
 */
public class EntryPositionTimeDaoImpl implements EntryPositionTimeDao {

    private static final Logger LOG = LoggerFactory.getLogger(EntryPositionTimeDaoImpl.class);

    @PersistenceContext(unitName = "primary")
    protected EntityManager em;

    @Resource
    protected UserTransaction tx;

    public List<EntryPositionTime> getPositionTimesForPositionInRace(Race race, Position position) throws DaoException {
        return new ArrayList<>();
    }

    public EntryPositionTime createTimeForEntry(EntryPositionTime time) throws DaoException {
        return null;
    }

}
