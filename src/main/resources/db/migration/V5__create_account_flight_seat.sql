CREATE TYPE owner_type AS ENUM ('AIRLINE', 'PASSENGER');
CREATE TYPE cabin_class AS ENUM ('ECONOMY', 'BUSINESS');

CREATE TABLE account
(
    id         UUID PRIMARY KEY,
    run_id     UUID           NOT NULL REFERENCES run (id) ON DELETE CASCADE,
    owner_type owner_type     NOT NULL,
    balance    NUMERIC(14, 2) NOT NULL DEFAULT 0,

    CONSTRAINT uq_account_run_id_owner_type UNIQUE (run_id, id, owner_type)
);

CREATE UNIQUE INDEX uq_account_airline_run ON account (run_id)
    WHERE owner_type = 'AIRLINE';

CREATE TABLE flight
(
    id             UUID PRIMARY KEY,
    run_id         UUID    NOT NULL REFERENCES run (id) ON DELETE CASCADE,
    economy_seats  INTEGER NOT NULL CHECK (economy_seats >= 0),
    business_seats INTEGER NOT NULL CHECK (business_seats >= 0),

    CONSTRAINT uq_flight_run UNIQUE (run_id),
    CONSTRAINT uq_flight_run_id UNIQUE (run_id, id)
);

CREATE TABLE seat
(
    id          UUID PRIMARY KEY,
    flight_id   UUID        NOT NULL REFERENCES flight (id) ON DELETE CASCADE,
    seat_number VARCHAR(12) NOT NULL,
    cabin_class cabin_class NOT NULL,

    CONSTRAINT uq_seat_flight_number UNIQUE (flight_id, seat_number),
    CONSTRAINT uq_seat_flight_class_id UNIQUE (flight_id, cabin_class, id)
);
