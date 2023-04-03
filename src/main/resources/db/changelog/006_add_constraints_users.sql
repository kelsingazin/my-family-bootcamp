--liquibase formatted sql
--changeset fufn:add-constaints-users
alter table users
    add constraint user_phone_number_uniqueness unique (phone_number);


