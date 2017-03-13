-- this is a template to allow event_position entries to be created for an event on its creation

CREATE TABLE race_position_template (
    id bigint NOT NULL PRIMARY KEY,
    name character varying(255) NOT NULL,
    default_template boolean,
    race_id bigint NOT NULL REFERENCES race (id)
);

ALTER TABLE race_position_template ADD CONSTRAINT race_position_template_constraint UNIQUE (race_id, name);

CREATE SEQUENCE race_position_template_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE race_position_template_entry (
    id bigint NOT NULL PRIMARY KEY,
    position_order integer NOT NULL,
    template_id bigint NOT NULL REFERENCES race_position_template (id),
    position_id bigint NOT NULL REFERENCES position (id)
);

CREATE SEQUENCE race_position_template_entry_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE race_position_template_entry ADD CONSTRAINT race_position_template_entry_constraint UNIQUE (template_id, position_id);
ALTER TABLE race_position_template_entry ADD CONSTRAINT race_position_template_entry_order_constraint UNIQUE (template_id, position_id, position_order);
