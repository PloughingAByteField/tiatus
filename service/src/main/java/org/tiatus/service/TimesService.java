package org.tiatus.service;

import org.tiatus.entity.*;

import java.util.List;

/**
 * Created by johnreynolds on 13/12/2014.
 */
public interface TimesService {
    void createTime(EntryPositionTime entryPositionTime) throws ServiceException;
    void updateTime(EntryPositionTime entryPositionTime) throws ServiceException;

    List<EntryPositionTime> getPositionTimesForPositionInRace(Race race, Position position) throws ServiceException;
    List<EntryPositionTime> getTimesForRace(Race race) throws ServiceException;
}
