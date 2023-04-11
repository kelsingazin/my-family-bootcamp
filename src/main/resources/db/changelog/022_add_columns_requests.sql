--liquibase formatted sql
--changeset fufn:alter-table-requests-22
alter table requests
    add column birth_date timestamp;
alter table requests
    add column country varchar(255);
alter table requests
    add column first_name varchar(255);
alter table requests
    add column last_name varchar(255);
alter table requests
    add column middle_name varchar(255);
alter table requests
    add column gender varchar(255);
alter table requests
    add column father_individual_id uuid;
alter table requests
    add column mother_individual_id uuid;

