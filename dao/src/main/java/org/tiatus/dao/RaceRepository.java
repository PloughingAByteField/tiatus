package org.tiatus.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import org.tiatus.entity.Race;

public interface RaceRepository extends JpaRepository<Race, Long> {
    List<Race> findByOrderByRaceOrder();
}
