--liquibase formatted sql
--changeset fufn:create-table-users-01
create table users (
                       id uuid not null,
                       first_name varchar(255),
                       last_name varchar(255),
                       password varchar(255),
                       phone_number varchar(255),
                       role varchar(255),
                       primary key (id)
);
