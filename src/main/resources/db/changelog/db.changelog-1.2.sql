-- liquibase formatted sql

--changeset Sauchanka:3
ALTER table users
    rename column createdat to created_at;
ALTER table users
    rename column updatedat to updated_at;

ALTER table payment_card
    rename column createdat to created_at ;

ALTER table payment_card
    rename column updatedat to updated_at;