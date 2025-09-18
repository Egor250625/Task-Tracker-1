--changeset egorivanov:1.1

CREATE TABLE IF NOT EXISTS users
(
    id       SERIAL PRIMARY KEY,
    email     VARCHAR(128) UNIQUE NOT NULL,
    password VARCHAR(128)        NOT NULL
);

CREATE TABLE IF NOT EXISTS tasks
(
    id          SERIAL PRIMARY KEY,
    title       VARCHAR(128) NOT NULL,
    description text,
    user_id     INT          NOT NULL REFERENCES users (id),
    status      VARCHAR(128) NOT NULL,
    done_at     TIMESTAMP
);
