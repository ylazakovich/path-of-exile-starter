CREATE TABLE analyzed_skills
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    name       VARCHAR(255),
    craft_cost DOUBLE NOT NULL,
    profit     DOUBLE NOT NULL
);
