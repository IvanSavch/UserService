-- liquibase formatted sql

--changeset Sauchanka:2
ALTER table users
    add column createdat timestamp;
ALTER table users
    add column updatedat timestamp;

ALTER table payment_card
    add column createdat timestamp;

ALTER table payment_card
    add column updatedat timestamp;