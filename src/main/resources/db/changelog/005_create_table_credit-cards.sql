--liquibase formatted sql
--changeset fufn:create-table-credit_cards-04
create table credit_cards (
                              id uuid not null,
                              alias varchar(255),
                              balance float8,
                              card_holder_name varchar(255),
                              is_deleted boolean,
                              expiration_date varchar(255),
                              number varchar(255),
                              security_code int4,
                              user_id uuid not null,
                              primary key (id)
);


