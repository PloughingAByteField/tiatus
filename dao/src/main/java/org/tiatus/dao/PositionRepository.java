package org.tiatus.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import org.tiatus.entity.Position;

public interface PositionRepository extends JpaRepository<Position, Long> {
    List<Position> findByOrderByName();
}
