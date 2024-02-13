package org.tiatus.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.tiatus.entity.Entry;
import org.tiatus.entity.Race;

public interface EntryRepository extends JpaRepository<Entry, Long> {
    List<Entry> findByRaceOrderByRaceOrder(Race race);

    @Modifying
    @Query(value = "delete from entry where race_id = :raceId)", nativeQuery = true)
    void deleteEntriesForRace(Long raceId);
}
