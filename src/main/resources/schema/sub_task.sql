--liquibase formatted sql

--changeset momont-igor:sub_task
CREATE TABLE sub_task
(
    id        bigint primary key,
    card_id   bigint references card,
    header    text not null,
    main_body text not null,
    is_done   bool default false
);