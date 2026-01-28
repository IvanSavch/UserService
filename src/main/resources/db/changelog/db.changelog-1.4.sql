-- liquibase formatted sql

--changeset Sauchanka:5
alter table payment_card
    add constraint payment_card_users_id_fk
        foreign key (user_id) references users;