-- liquibase formatted sql

-- changeset usov:8
CREATE TABLE animal
(
    id           SERIAL PRIMARY KEY,
    animal_type  SMALLINT,
    animal_name  TEXT
);

-- changeset andrew:2
CREATE TABLE shelters
(
    id            SERIAL PRIMARY KEY,
    name          TEXT,
    address       TEXT,
    opening_hours TEXT
);

-- changeset usov:3
CREATE TABLE animal_photo
(
    id         SERIAL PRIMARY KEY,
    file_path  VARCHAR(255),
    file_size  BIGINT NOT NULL,
    media_type TEXT,
    data       BYTEA
);

-- changeset andrew:4
CREATE TABLE users
(
    id          SERIAL PRIMARY KEY,
    user_name   TEXT,
    chat_id     BIGINT,
    phone       TEXT,
    date        TIMESTAMP,
    animal_id   BIGINT REFERENCES animal (id),
    isVolunteer BOOLEAN,
    volunteer_chat_id BIGINT
);

-- changeset usov:10
CREATE TABLE report
(
    id              SERIAL PRIMARY KEY,
    name            TEXT,
    date            TIMESTAMP,
    diet            TEXT,
    health          TEXT,
    behavior        TEXT,
    user_id         BIGINT REFERENCES users (id),
    animal_photo_id BIGINT REFERENCES animal_photo (id)
);







