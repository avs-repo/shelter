-- liquibase formatted sql

-- changeset andrew:3
CREATE TABLE shelters (
    id SERIAL,
    name TEXT,
    address TEXT,
    openingHours TEXT
)