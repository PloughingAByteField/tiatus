package org.tiatus.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.tiatus.entity.EntryPositionId;
import org.tiatus.entity.EntryPositionTime;

public interface EntryPositionTimeRepository extends JpaRepository<EntryPositionTime, EntryPositionId> {
    @Query(value = "select * from entry_position_time ept where ept.position_id = :positionId and ept.entry_id in (select id from entry where race_id = :raceId)", nativeQuery = true)
    List<EntryPositionTime> getPositionTimesForPositionInRace(Long positionId, Long raceId);

    @Query(value = "select * from entry_position_time ept where ept.entry_id in (select id from entry where race_id = :raceId) order by ept.position_id", nativeQuery = true)
    List<EntryPositionTime> getAllTimesForRace(Long raceId);

    @Query(value = "select * from entry_position_time ept where ept.entry_id in (select e.id from Entry e, Disqualification d where e.race_id = :raceId and e.id != d.entry_id) order by ept.position_id", nativeQuery = true)
    List<EntryPositionTime> getTimesForRace(Long raceId);

    @Query(value = "select * from entry_position_time where position_id = :positionId and entry_id = :entryId", nativeQuery = true)
    List<EntryPositionTime> getTimesForEntryAtPosition(Long entryId, Long positionId);
}

