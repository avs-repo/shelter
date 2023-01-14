-- liquibase formatted sql

-- changeset andrew:5
CREATE TABLE users (
    id SERIAL,
    user_name TEXT,
    chat_id int8 UNIQUE,
    phone TEXT
)