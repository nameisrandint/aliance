--liquibase formatted sql

--changeset momont-igor:key_value
CREATE TABLE key_value
(
    key   text primary key,
    value text
);