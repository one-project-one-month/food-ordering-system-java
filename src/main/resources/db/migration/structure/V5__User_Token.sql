CREATE TABLE user_tokens
(
    id            BIGINT NOT NULL AUTO_INCREMENT,
    createdAt     DATETIME(6) NOT NULL,
    updatedAt     DATETIME(6),
    deletedAt     DATETIME(6),
    status        INTEGER,
    token VARCHAR(255) NOT NULL,
    username   VARCHAR(255) NOT NULL,
    password   VARCHAR(255) NOT NULL,
    CONSTRAINT pk_user_token PRIMARY KEY (id)
);