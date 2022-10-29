--liquibase formatted sql

--changeset momont-igor:status
CREATE TABLE status
(
    id   bigint primary key,
    name text not null
);