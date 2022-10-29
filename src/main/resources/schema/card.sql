--liquibase formatted sql

--changeset momont-igor:card
CREATE TABLE card
(
    id           bigint primary key,
    title        text    not null,
    difficulty   integer not null,
    status_id    bigint  not null references status,
    card_type_id bigint    not null references card_type,
    level_id        bigint references level
);