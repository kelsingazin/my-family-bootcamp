--liquibase formatted sql
--changeset fufn:create-table-individuals-02
create table individuals (
                             id uuid not null,
                             birth_date timestamp not null,
                             first_name varchar(255),
                             home_city varchar(255) not null,
                             iin varchar(12) not null,
                             last_name varchar(255),
                             marital_status varchar(255),
                             middle_name varchar(255),
                             nationality varchar(255) not null,
                             phone_number varchar(255),
                             photo varchar(255) not null,
                             primary key (id)
);

