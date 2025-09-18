--changeset egorivanov:1.2

CREATE TABLE IF NOT EXISTS user_events (
    id              SERIAL      PRIMARY KEY,
    pull_count      INTEGER     DEFAULT 0 NOT NULL,
    enqueued_at     TIMESTAMP   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    reserved_to     TIMESTAMP   NOT NULL,
    message         JSONB       NOT NULL
);