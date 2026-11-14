CREATE TABLE airport (
                         iata_code VARCHAR(3) PRIMARY KEY,
                         name      VARCHAR(255) NOT NULL,
                         city      VARCHAR(255) NOT NULL,
                         country   VARCHAR(255) NOT NULL,
                         latitude  NUMERIC(9, 6) NOT NULL,
                         longitude NUMERIC(9, 6) NOT NULL,
                         timezone  VARCHAR(255) NOT NULL
);
