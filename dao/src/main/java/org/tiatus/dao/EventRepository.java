package org.tiatus.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.tiatus.entity.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByOrderById();

    @Query(value = "select * from event e where e.id not in (select re.event_id FROM race_event re)", nativeQuery = true)
    List<Event> getUnassignedEvents();
}
