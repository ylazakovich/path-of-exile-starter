CREATE TABLE users
(
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id         BIGINT       NOT NULL,
    username        VARCHAR(255) NOT NULL,
    last_message_id INT
);