package org.tiatus.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.tiatus.entity.Event;
import org.tiatus.entity.RaceEvent;

public interface RaceEventRepository extends JpaRepository<RaceEvent, Long> {
    @Query(value = "select * from race_event re where re.race_id = :raceId order by race_event_order", nativeQuery = true)
    List<RaceEvent> getRaceEventsForRace(Long raceId);

    List<RaceEvent> findByOrderByRaceEventOrder();

    RaceEvent findByEvent(Event event);
}
