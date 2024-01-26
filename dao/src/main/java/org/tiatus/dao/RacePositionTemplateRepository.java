package org.tiatus.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import org.tiatus.entity.RacePositionTemplate;

public interface RacePositionTemplateRepository extends JpaRepository<RacePositionTemplate, Long> {

    List<RacePositionTemplate> findByOrderByRace();
}
