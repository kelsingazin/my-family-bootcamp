--liquibase formatted sql
--changeset kelsingazin:insert-table-individuals
INSERT INTO individuals
VALUES
('684220fd-bf5b-462c-9d87-7460ba21f5c1', '2000-04-06 15:58:12.000000', 'Danil', 'Almaty', '111111', 'Kim', 'MARRIED', 'middle', 'nationality' , '87771111111', '111111', 'MALE'),
('82efd2af-9cf3-44a1-a396-805ebb77951e', '2001-11-21 06:00:00.000000', 'Yelena', 'Almaty', '222222', 'Tussupzhanova', 'SINGLE', 'middle', 'nationality' , '87772222222', '222222', 'FEMALE');
