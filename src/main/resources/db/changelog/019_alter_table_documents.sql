--liquibase formatted sql
--changeset kelsingazin:alter_table_documents
ALTER TABLE documents
ADD COLUMN is_deleted boolean
NOT NULL DEFAULT false;