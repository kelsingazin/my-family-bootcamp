--liquibase formatted sql
--changeset fufn:create-table-documents-04
create table documents (
                           id uuid not null,
                           type varchar(255) not null,
                           driver_license_category varchar(255),
                           expiration_date timestamp not null,
                           issue_date timestamp not null,
                           issuing_authority varchar(255),
                           license_number varchar(255),
                           series varchar(255),
                           individual_id uuid not null,
                           user_id uuid not null,
                           primary key (id)
);


