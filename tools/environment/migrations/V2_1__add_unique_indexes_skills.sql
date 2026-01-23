ALTER TABLE skills
    ADD CONSTRAINT uq_skills_league_name_variant
        UNIQUE (league_id, name, variant);
