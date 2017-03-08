
CREATE TABLE event_position (
    position_order integer NOT NULL,
    can_start boolean,
    finishing_position boolean,
    starting_position boolean,
    event_id bigint NOT NULL REFERENCES event (id),
    position_id bigint NOT NULL REFERENCES position (id)
);

ALTER TABLE event_position ADD CONSTRAINT event_position_order_constraint UNIQUE (event_id, position_id, position_order);
