--liquibase formatted sql
--changeset fufn:add-constaints-offices
alter table offices
    add constraint fk_city_office
        foreign key (city_id)
            references cities;






