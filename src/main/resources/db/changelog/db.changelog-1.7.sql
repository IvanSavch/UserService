-- liquibase formatted sql

--changeset Sauchanka:8
alter table users add column auth_id BIGINT;

create index users_auth_id_index
    on users (auth_id);