--liquibase formatted sql
--changeset fufn:add-constaints-requests
alter table requests
    add constraint fk_requests_users_request
        foreign key (request_user_id)
            references users;
alter table requests
    add constraint fk_requests_users_response
        foreign key (response_user_id)
            references users;






