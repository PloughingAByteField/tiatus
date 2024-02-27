CREATE TABLE role (
    id bigint NOT NULL PRIMARY KEY,
    role character varying(255) NOT NULL UNIQUE
);

CREATE SEQUENCE role_id_sequence
    START WITH 3
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

COPY role (id, role) FROM stdin;
1	ROLE_ADMIN
2	ROLE_TIMING
3	ROLE_ADJUDICATOR
\.

SELECT setval('role_id_sequence', max(id)) FROM role;