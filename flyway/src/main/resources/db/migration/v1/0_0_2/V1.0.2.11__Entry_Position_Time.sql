CREATE TABLE entry_position_time (
    start_point boolean,
    synced boolean,
    "time" timestamp without time zone,
    entry_id bigint NOT NULL REFERENCES entry (id),
    position_id bigint NOT NULL REFERENCES "position" (id)
);

ALTER TABLE entry_position_time ADD CONSTRAINT entry_position_time_constraint UNIQUE (entry_id, position_id);
