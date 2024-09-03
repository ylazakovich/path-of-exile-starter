CREATE TABLE rates
(
    id               BIGINT PRIMARY KEY AUTO_INCREMENT,
    league_id        BIGINT       NOT NULL,
    name             VARCHAR(255) NOT NULL,
    chaos_equivalent DOUBLE       NOT NULL,
    FOREIGN KEY (league_id) REFERENCES leagues (id)
);
