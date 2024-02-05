package org.tiatus.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.tiatus.entity.Disqualification;

public interface DisqualificationRepository extends JpaRepository<Disqualification, Long> {
    
    @Modifying
    @Query(value = "update disqualification set entry_id = :toId where entry_id = :fromId", nativeQuery = true)
    void updateDisqualificationEntryId(Long fromId, Long toId);    
}
