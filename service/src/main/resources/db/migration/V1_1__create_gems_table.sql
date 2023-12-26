CREATE TABLE gems
(
    id         INTEGER PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    variant    VARCHAR(255) NOT NULL,
    corrupted  BOOLEAN      NOT NULL,
    gemLevel   INTEGER      NOT NULL,
    gemQuality INTEGER      NOT NULL,
    chaosValue DOUBLE NOT NULL
);