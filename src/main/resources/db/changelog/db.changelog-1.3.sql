-- liquibase formatted sql

--changeset Sauchanka:4
ALTER table payment_card
    alter column number type varchar(255);