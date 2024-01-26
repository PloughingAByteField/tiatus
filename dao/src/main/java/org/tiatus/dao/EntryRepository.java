package org.tiatus.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tiatus.entity.Entry;
import org.tiatus.entity.Race;

public interface EntryRepository extends JpaRepository<Entry, Long> {
    List<Entry> findByRaceOrderByRaceOrder(Race race);
}
