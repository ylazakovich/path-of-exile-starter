CREATE TABLE vendor_recipes
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    league_id  BIGINT       NOT NULL,
    item_name  VARCHAR(255) NOT NULL,
    craft_cost DOUBLE       NOT NULL,
    sell_price DOUBLE       NOT NULL,
    FOREIGN KEY (league_id) REFERENCES leagues (id)
);
