CREATE TABLE skills
(
    id                     BIGINT PRIMARY KEY AUTO_INCREMENT,
    league_id              BIGINT       NOT NULL,
    name                   VARCHAR(255) NOT NULL,
    variant                VARCHAR(255) NOT NULL,
    corrupted              BOOLEAN      NOT NULL,
    gem_level              INTEGER      NOT NULL,
    gem_quality            INTEGER      NOT NULL,
    chaos_equivalent_price DOUBLE       NOT NULL,
    FOREIGN KEY (league_id) REFERENCES leagues (id)
);
