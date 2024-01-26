package org.tiatus.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.tiatus.entity.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByOrderById();

    @Query(value = "FROM Event e where e.id not in (select re.event FROM RaceEvent re)", nativeQuery = true)
    List<Event> getUnassignedEvents();
}
