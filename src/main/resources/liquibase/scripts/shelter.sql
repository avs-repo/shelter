-- liquibase formatted sql

-- changeset andrew:2
CREATE TABLE shelters (
    id SERIAL,
    name TEXT,
    info TEXT
)