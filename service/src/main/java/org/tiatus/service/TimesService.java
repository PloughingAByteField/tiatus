package org.tiatus.service;

import org.tiatus.entity.*;

import java.util.List;

/**
 * Created by johnreynolds on 13/12/2014.
 */
public interface TimesService {
    EntryPositionTime createTime(EntryPositionTime entryPositionTime, String sessionId) throws ServiceException;
    EntryPositionTime updateTime(EntryPositionTime entryPositionTime, String sessionId) throws ServiceException;

    List<EntryPositionTime> getPositionTimesForPositionInRace(Race race, Position position) throws ServiceException;
    List<EntryPositionTime> getTimesForRace(Race race) throws ServiceException;
    List<EntryPositionTime> getAllTimesForRace(Race race) throws ServiceException;
}
