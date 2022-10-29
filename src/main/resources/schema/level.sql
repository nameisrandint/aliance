--liquibase formatted sql

--changeset momont-igor:level
CREATE TABLE level
(
    id        bigint primary key,
    is_active bool not null default false,
    user_id   bigint references aliance_user
);