DELETE FROM meals;
DELETE FROM user_role;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin'),
       ('Guest', 'guest@gmail.com', 'guest');

INSERT INTO user_role (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO meals (user_id, date_time, description, calories)
VALUES (100000, '2023-06-22 8:00:00', 'завтрак', 1000),
       (100000, '2023-06-22 13:00:00', 'обед', 1000),
       (100000, '2023-06-22 19:00:00', 'ужин', 500),
       (100001, '2023-06-22 10:00:00', 'завтрак', 500),
       (100001, '2023-06-22 14:00:00', 'обед', 1000),
       (100001, '2023-06-22 20:00:00', 'ужин', 500);
