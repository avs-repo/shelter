-- liquibase formatted sql

-- changeset andrew:1
CREATE TABLE users
(
    id     SERIAL,
    name   TEXT,
    chatId int8,
    phone  TEXT

)

-- changeset andrew: 2
CREATE TABLE shelters
(
    id           SERIAL,
    name         TEXT,
    address      TEXT,
    openingHours TEXT
);

-- changeset usov: 3
CREATE TABLE animalPhoto
(
    id        SERIAL PRIMARY KEY,
    file_path VARCHAR(255),
    file_size BIGINT NOT NULL,
    mediaType TEXT,
    data      BYTEA
);

-- changeset usov: 4
CREATE TYPE animal_type AS ENUM('DOG','CAT');

-- changeset usov: 5
CREATE TABLE animal
(
    id          SERIAL PRIMARY KEY,
    animal_type animal_type,
    animal_name TEXT

);

-- changeset usov: 6
CREATE TABLE report
(
    id             SERIAL PRIMARY KEY,
    name           TEXT,
    date           TIMESTAMP,
    diet           TEXT,
    health         TEXT,
    behavior       TEXT,
    user_id        BIGINT REFERENCES users (id),
    animalPhoto_id BIGINT REFERENCES animal (id),
);





