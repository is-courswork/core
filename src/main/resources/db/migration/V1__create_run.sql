CREATE TABLE run
(
    id          UUID PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description TEXT,
    status      VARCHAR(16)  NOT NULL,
    created_at  TIMESTAMPTZ  NOT NULL,
    started_at  TIMESTAMPTZ,
    finished_at TIMESTAMPTZ
);
