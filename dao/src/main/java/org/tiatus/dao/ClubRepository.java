package org.tiatus.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tiatus.entity.Club;

@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {

    
}
