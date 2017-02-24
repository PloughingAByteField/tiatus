
ALTER TABLE event ADD COLUMN starting_position bigint NOT NULL;
ALTER TABLE event ADD CONSTRAINT fk_starting_position FOREIGN KEY (starting_position) REFERENCES position (id);
ALTER TABLE event ADD COLUMN finishing_position bigint NOT NULL;
ALTER TABLE event ADD CONSTRAINT fk_finishing_position FOREIGN KEY (finishing_position) REFERENCES position (id);