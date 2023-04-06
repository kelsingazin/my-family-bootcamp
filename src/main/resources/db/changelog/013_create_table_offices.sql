--liquibase formatted sql
--changeset fufn:create-table-credit_offices
create table offices (
                              id uuid not null,
                              name varchar(255),
                              city_id uuid,
                              primary key (id)
);


