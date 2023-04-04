--liquibase formatted sql
--changeset fufn:create-table-credit_offices
create table cities (
                              id uuid not null,
                              name varchar(255),
                              primary key (id)
);


