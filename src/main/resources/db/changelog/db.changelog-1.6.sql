-- liquibase formatted sql

--changeset Sauchanka:7
ALTER table Users add unique (email);
ALTER table payment_card add unique (number);