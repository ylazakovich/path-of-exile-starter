CREATE TABLE processed_skills
(
    id                      BIGINT PRIMARY KEY AUTO_INCREMENT,
    league_id               BIGINT NOT NULL,
    skill_id                BIGINT NOT NULL,
    chaos_equivalent_price  DOUBLE NOT NULL,
    chaos_equivalent_profit DOUBLE NOT NULL,
    FOREIGN KEY (league_id) REFERENCES leagues (id),
    FOREIGN KEY (skill_id) REFERENCES skills (id)
);
