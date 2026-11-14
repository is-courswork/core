CREATE TYPE run_status AS ENUM (
    'DRAFT',
    'RUNNING',
    'PAUSED',
    'FINISHED',
    'CANCELLED',
    'FAILED'
);

ALTER TABLE run
    ALTER COLUMN status TYPE run_status USING status::run_status;
