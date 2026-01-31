-- liquibase formatted sql

--changeset Sauchanka:6
create index payment_card_user_id_index
    on payment_card (user_id);