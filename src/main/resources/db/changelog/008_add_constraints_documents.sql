--liquibase formatted sql
--changeset fufn:add-constaints-documents
alter table documents
    add constraint license_number_uniqueness unique (license_number);
alter table documents
    add constraint series_uniqueness unique (series);
alter table documents
    add constraint fk_documents_individuals
        foreign key (individual_id)
            references individuals;
alter table documents
    add constraint fk_documents_users
        foreign key (user_id)
            references users;





