CREATE TABLE user_role (
    id bigint NOT NULL PRIMARY KEY,
    role_id bigint NOT NULL,
    user_id bigint NOT NULL
);

ALTER TABLE user_role ADD CONSTRAINT role_user_constraint UNIQUE (role_id, user_id);


CREATE SEQUENCE user_role_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;