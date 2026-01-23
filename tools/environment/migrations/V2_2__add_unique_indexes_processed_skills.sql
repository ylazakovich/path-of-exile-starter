ALTER TABLE processed_skills
    ADD CONSTRAINT uq_processed_skills_league_skill
        UNIQUE (league_id, skill_id);
