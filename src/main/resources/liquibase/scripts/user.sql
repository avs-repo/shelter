-- liquibase formatted sql

-- changeset andrew:1
CREATE TABLE users (
    id SERIAL,
    name TEXT,
    chatId int8,
    phone TEXT
)