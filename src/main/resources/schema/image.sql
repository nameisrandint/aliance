--liquibase formatted sql

--changeset momont-igor:image
CREATE TABLE image
(
    name varchar primary key,
    content bytea
);

--changeset momont-igor:image_updated_at
alter table image add column updated_at timestamp default now();


--changeset momont-igor:image_id
alter table image add column id bigserial;
create unique index image_id on image (id);
