--liquibase formatted sql
--changeset kelsingazin:insert-table-users-01
INSERT INTO users
VALUES
('4fad3fde-aba2-4002-8f56-17ebae75d2f1', 'Danil', 'Kim', '$2a$10$6bnfFOAUHwoXn3T5Q/ND8OTk3/UVBEzVkQ.YntiHfg33gQrgw9X0m', '87771111111', 'ROLE_USER'),
('f6b2626a-39b0-405f-a929-63045af6f220', 'Yelena', 'Tussupzhanova', '$2a$10$6bnfFOAUHwoXn3T5Q/ND8OTk3/UVBEzVkQ.YntiHfg33gQrgw9X0m', '87772222222', 'ROLE_USER'),
('9e7326b3-6645-4ac7-88b9-f7f90b16ff2a', 'Admin', 'Adminov', '$2a$10$6bnfFOAUHwoXn3T5Q/ND8OTk3/UVBEzVkQ.YntiHfg33gQrgw9X0m', '87770000000', 'ROLE_ADMIN');
