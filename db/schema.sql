CREATE TABLE account (
                         id SERIAL PRIMARY KEY,
                         username VARCHAR NOT NULL,
                         email VARCHAR NOT NULL UNIQUE,
                         phone VARCHAR NOT NULL UNIQUE
);

CREATE TABLE ticket (
                        id SERIAL PRIMARY KEY,
                        session_id INT NOT NULL,
                        row INT NOT NULL,
                        cell INT NOT NULL,
                        account_id INT NOT NULL REFERENCES account(id)
);

CREATE UNIQUE INDEX idx_cinema_seat
    ON ticket(session_id, row, cell);

ALTER TABLE IF EXISTS ticket
    RENAME TO ticker;

ALTER TABLE ticker
    RENAME COLUMN cell TO columnn;

ALTER TABLE ticker
    ADD CONSTRAINT const UNIQUE (session_id, row, columnn);