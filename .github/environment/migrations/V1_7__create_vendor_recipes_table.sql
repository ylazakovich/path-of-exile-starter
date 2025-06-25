CREATE TABLE vendor_recipes
(
    id                      BIGINT PRIMARY KEY AUTO_INCREMENT,
    league_id               BIGINT       NOT NULL,
    name                    VARCHAR(255) NOT NULL,
    chaos_equivalent_price  DOUBLE       NOT NULL,
    chaos_equivalent_profit DOUBLE       NOT NULL,
    FOREIGN KEY (league_id) REFERENCES leagues (id)
);
