--liquibase formatted sql
--changeset fufn:add-column-gender-individuals
alter table individuals
add gender varchar(255);

