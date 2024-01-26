package org.tiatus.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import org.tiatus.entity.EventPosition;

public interface EventPositionRepository extends JpaRepository<EventPosition, Long> {

}
