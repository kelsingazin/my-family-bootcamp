--liquibase formatted sql
--changeset fufn:add-constaints-credit_cards
alter table credit_cards
    add constraint fk_credit_cards_users
        foreign key (user_id)
            references users;







