--liquibase formatted sql
--changeset fufn:alter-table-requests-23
alter table requests
    add constraint fk_father_requests
        foreign key (father_individual_id)
            references individuals;
alter table requests
    add constraint fk_mother_requests
        foreign key (mother_individual_id)
            references individuals;
alter table requests
    alter column response_user_id drop not null;