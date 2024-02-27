CREATE TABLE entry_club_xref (
    entry_id bigint NOT NULL REFERENCES entry (id),
    club_id bigint NOT NULL REFERENCES club (id)
);

ALTER TABLE entry_club_xref ADD CONSTRAINT entry_club_xref_constraint UNIQUE (entry_id, club_id);
