package org.tiatus.service;

import org.tiatus.dao.DaoException;
import org.tiatus.dao.EntryPositionTimeDao;
import org.tiatus.entity.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.util.*;

@Default
public class TimesServiceImpl implements TimesService {

    private static final Logger LOG = LoggerFactory.getLogger(TimesServiceImpl.class);

    private final EntryPositionTimeDao dao;

    @Inject
    public TimesServiceImpl(EntryPositionTimeDao dao) {
        this.dao = dao;
    }

    @Override
    public void createTime(EntryPositionTime pt) throws ServiceException {
//        ie.galwayheadoftheriver.splittiming.dao.PositionTime positionTime = new ie.galwayheadoftheriver.splittiming.dao.PositionTime();
//        try {
//            Position position = positionsDao.getPositionForId(pt.getPositionId().toString());
//            positionTime.setPosition(position);
//
//            Entry entry = entriesDao.getEntryForId(pt.getEntryTime().getEntryId().toString());
//            positionTime.setEntry(entry);
//            logger.debug("Creating time for " + positionTime.getPosition().getName() + " race " + positionTime.getEntry().getRace().getName());
//            positionTime.setStartPoint(pt.isStartPoint());
//            positionTime.setTime(pt.getEntryTime().getTime());
//            positionTime.setSynced(true);
//            dao.addPositionTime(positionTime);
//
//            if (messageSenderService == null) {
//                logger.warn("null for message sender");
//            }
//            logger.debug("will send " + pt);
//            // add to the queue
//            messageSenderService.sendMessage(pt, MessageType.ADD);
//
//        } catch (DaoConstraintException e) {
//            logger.warn("Got constaint exception");
//            throw new ServiceException();
//        } catch (DaoException e) {
//            logger.warn("Got dao exception");
//            throw new ServiceException();
//        } catch (JMSException e) {
//            logger.warn("Got jms exception", e);
//            throw new ServiceException();
//        }
        LOG.debug("after dao");
    }

    @Override
    public List<EntryPositionTime> getPositionTimesForPositionInRace(Race race, Position position) throws ServiceException {
        LOG.debug("Get list of times for race " + race.getName() + " at position " + position.getName());
        try {
            List<EntryPositionTime> positionTimes = dao.getPositionTimesForPositionInRace(race, position);
//            List<EntryPositionTime> positionTimes = dao.getTimes(race);
//            List<Entry> entries = entriesDao.getEntriesForRace(race);
//            List<EntryTime> times = new ArrayList<>();
//            for (Entry entry : entries) {
//                EntryTime entryTime = new EntryTime();
//                entryTime.setNumber(entry.getNumber());
//                entryTime.setEvent(entry.getEvent().getName());
//                entryTime.setCrew(entry.getCrew());
//                entryTime.setEntryId(entry.getId());
//
//                StringBuffer clubList = new StringBuffer();
//                for (Club c : entry.getClubs()){
//                    if (clubList.length() > 0) {
//                        clubList.append("/");
//                    }
//                    clubList.append(c.getClub());
//                }
//                entryTime.setClub(clubList.toString());
//
//                for (ie.galwayheadoftheriver.splittiming.dao.PositionTime pt : positionTimes) {
//                    if (pt.getEntry().getId().equals(entry.getId())) {
//                        if (pt.isStartPoint() != null && pt.isStartPoint()) {
//                            entryTime.setStarted(pt.isStartPoint());
//                        }
//
//                        if (pt.getPosition().getId() == position.getId()) {
//                            entryTime.setTime(pt.getTime());
//                            entryTime.setSynced(pt.getSynced());
//                        }
//                    }
//                }
//
//                times.add(entryTime);
//            }

            return positionTimes;

        } catch (DaoException e) {
            LOG.warn("Got dao exception");
            throw new ServiceException(e);
        }
    }


}
