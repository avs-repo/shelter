-- liquibase formatted sql

-- changeset andrew:1
CREATE TABLE users
(
    id          SERIAL PRIMARY KEY,
    user_name   TEXT,
    chat_id     BIGINT,
    phone       TEXT

);

-- changeset andrew:2
CREATE TABLE shelters
(
    id           SERIAL PRIMARY KEY,
    name         TEXT,
    address      TEXT,
    opening_hours TEXT
);

-- changeset usov:3
CREATE TABLE animal_photo
(
    id        SERIAL PRIMARY KEY,
    file_path VARCHAR(255),
    file_size BIGINT NOT NULL,
    media_type TEXT,
    data      BYTEA
);

-- changeset usov:4
CREATE TYPE animal_type AS ENUM('DOG','CAT');

-- changeset usov:5
CREATE TABLE animal
(
    id          SERIAL PRIMARY KEY,
    animal_type animal_type,
    animal_name TEXT

);

-- changeset usov:6
CREATE TABLE report
(
    id              SERIAL PRIMARY KEY,
    name            TEXT,
    date            TIMESTAMP,
    diet            TEXT,
    health          TEXT,
    behavior        TEXT,
    user_id         BIGINT REFERENCES users (id),
    animal_photo_id BIGINT REFERENCES animal (id)
);




