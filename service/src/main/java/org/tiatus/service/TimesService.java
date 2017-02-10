package org.tiatus.service;

import org.tiatus.entity.*;

import java.util.List;

/**
 * Created by johnreynolds on 13/12/2014.
 */
public interface TimesService {
    void createTime(EntryPositionTime time) throws ServiceException;

    List<EntryPositionTime> getPositionTimesForPositionInRace(Race race, Position position) throws ServiceException;
}
