--liquibase formatted sql

--changeset momont-igor:card_type
CREATE TABLE card_type
(
    id    bigint primary key,
    block text not null,
    color text not null,
    icon  text not null
);