package org.tiatus.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import org.tiatus.entity.Penalty;

public interface PenaltyRepository extends JpaRepository<Penalty, Long> {

}
