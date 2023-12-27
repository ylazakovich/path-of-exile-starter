CREATE TABLE gems
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    name       VARCHAR(255) NOT NULL,
    variant    VARCHAR(255) NOT NULL,
    corrupted  BOOLEAN      NOT NULL,
    gemLevel   INTEGER      NOT NULL,
    gemQuality INTEGER      NOT NULL,
    chaosValue DOUBLE       NOT NULL
);