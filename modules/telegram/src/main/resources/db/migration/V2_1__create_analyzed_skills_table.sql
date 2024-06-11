CREATE TABLE skills
(
    id                      BIGINT PRIMARY KEY AUTO_INCREMENT,
    name                    VARCHAR(255),
    chaos_equivalent_price  DOUBLE NOT NULL,
    chaos_equivalent_profit DOUBLE NOT NULL
);
