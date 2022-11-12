--liquibase formatted sql

--changeset momont-igor:update
insert into aliance_user (id) values (0);

insert into status (id, name) values (1, 'NEW'),
                                 (2, 'IN_PROGRESS'),
                                 (3, 'DONE');


insert into level (id, user_id) values (0, 0);

insert into card_type (id, block, color, icon) values
    (1, 'super block', 'super color', 'super icon name');

insert into card (id, title, difficulty, status_id, card_type_id, level_id)
values (1, 'super card title', '100', '1', '1', 0);

insert into sub_task (id, card_id, header, main_body)
values (1, 1, 'super header text', 'super main body');

--changeset momont-igor:update2
insert into key_value (key, value)
values ('committed', 'true');