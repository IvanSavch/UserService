-- liquibase formatted sql

--changeset Sauchanka:1
create table users
(
    id         BIGSERIAL    not null,
    name       varchar(255) not null,
    surname    varchar(255) not null,
    birth_date date         not null,
    email      varchar      not null,
    active     boolean      not null

);

create index "users_id_index"
    on users (id);

alter table users
    add constraint "users_pk"
        primary key (id);

create table payment_card
(
    id              Bigserial not null,
    user_id         BIGINT    not null,
    number          integer   not null,
    holder          varchar   not null,
    expiration_date date      not null,
    active          boolean   not null

);

create unique index payment_card_id_uindex
    on payment_card (id);

alter table payment_card
    add constraint payment_card_pk
        primary key (id);
