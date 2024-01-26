package org.tiatus.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tiatus.entity.RacePositionTemplateEntry;
import org.tiatus.entity.RacePositionTemplate;
import org.tiatus.entity.Position;

public interface RacePositionTemplateEntryRepository extends JpaRepository<RacePositionTemplateEntry, Long> {
    List<RacePositionTemplateEntry> findByTemplate(RacePositionTemplate template);

    RacePositionTemplateEntry findByTemplateAndPosition(RacePositionTemplate template, Position position);
}
