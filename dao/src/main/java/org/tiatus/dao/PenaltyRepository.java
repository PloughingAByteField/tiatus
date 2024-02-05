package org.tiatus.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.tiatus.entity.Penalty;

public interface PenaltyRepository extends JpaRepository<Penalty, Long> {

    @Modifying
    @Query(value = "update penalty set entry_id = :toId where entry_id = :fromId", nativeQuery = true)
    void updatePenaltyEntryId(Long fromId, Long toId);
}
