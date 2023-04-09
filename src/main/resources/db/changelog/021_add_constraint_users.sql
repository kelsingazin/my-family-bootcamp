--liquibase formatted sql
--changeset kelsingazin:add_constraint_users
alter table users
add constraint user_email_uniqueness unique (email);
