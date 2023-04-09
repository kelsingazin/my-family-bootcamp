--liquibase formatted sql
--changeset kelsingazin:alter_table_users
ALTER TABLE users
ADD COLUMN email varchar(255) NOT NULL
DEFAULT 'myemail@gmail.com';
