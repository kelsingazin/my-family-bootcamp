--liquibase formatted sql
--changeset fufn:add-constaints-individuals
alter table individuals
    add constraint individual_phone_number_uniqueness unique (phone_number);



