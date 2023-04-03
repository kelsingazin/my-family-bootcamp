--liquibase formatted sql
--changeset fufn:create-table-requests-03
create table requests (
                          id uuid not null,
                          city varchar(255),
                          date timestamp,
                          is_partner_paid boolean,
                          office varchar(255),
                          status varchar(255),
                          type varchar(255),
                          request_user_id uuid not null,
                          response_user_id uuid not null,
                          primary key (id)
);


