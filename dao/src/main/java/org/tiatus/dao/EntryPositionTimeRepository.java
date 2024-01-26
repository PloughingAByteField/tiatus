package org.tiatus.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.tiatus.entity.EntryPositionId;
import org.tiatus.entity.EntryPositionTime;

public interface EntryPositionTimeRepository extends JpaRepository<EntryPositionTime, EntryPositionId> {
    @Query(value = "FROM EntryPositionTime ept where ept.position.id = :positionId and ept.entry in (select id from Entry where race.id = :raceId)", nativeQuery = true)
    List<EntryPositionTime> getPositionTimesForPositionInRace(Long positionId, Long raceId);

    @Query(value = "FROM EntryPositionTime ept where ept.entry in (select id from Entry where race.id = :raceId) order by ept.position.id", nativeQuery = true)
    List<EntryPositionTime> getAllTimesForRace(Long raceId);

    @Query(value = "FROM EntryPositionTime ept where ept.entry in (select e.id from Entry e, Disqualification d where e.race.id = :raceId and e.id != d.entry.id) order by ept.position.id", nativeQuery = true)
    List<EntryPositionTime> getTimesForRace(Long raceId);

    @Query(value = "FROM EntryPositionTime where position.id = :positionId and entry.id = :entryId", nativeQuery = true)
    List<EntryPositionTime> getTimesForEntryAtPosition(Long entryId, Long positionId);
}

