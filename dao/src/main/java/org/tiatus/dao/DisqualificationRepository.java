package org.tiatus.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tiatus.entity.Disqualification;

public interface DisqualificationRepository extends JpaRepository<Disqualification, Long> {
    
}
