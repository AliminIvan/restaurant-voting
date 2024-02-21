INSERT INTO USERS (name, email, password)
VALUES ('User', 'user@yandex.ru', '{noop}password'),
       ('Admin', 'admin@gmail.com', '{noop}admin'),
       ('Guest', 'guest@gmail.com', '{noop}guest');

INSERT INTO USER_ROLE (role, user_id)
VALUES ('USER', 1),
       ('ADMIN', 2),
       ('USER', 2);

INSERT INTO RESTAURANT (name, address)
VALUES ('Pasta Na Solyanke', 'Solyanka, 2/6, Bldg. 1, Moscow 109240 Russia'),
       ('Dzhumbus', 'Dobrovolcheskaya St.,12, Moscow 109004 Russia'),
       ('Anderson', 'Rublevskoye Hwy., 28/1, Moscow 613310 Russia'),
       ('Anderson', 'Lva Tolstogo, 23/1 floor 2, Moscow 119021 Russia'),
       ('Bryanskiy Byk', 'Bldg. 1 Bolshaya Lubyanka St. 24/15, Moscow 101000 Russia');

INSERT INTO MENU (lunch_date, restaurant_id)
VALUES (current_date, 1),
       (current_date, 2),
       (current_date, 3),
       (current_date, 4),
       (current_date, 5),
       ('2023-12-31', 2);

INSERT INTO DISH (name, price, menu_id)
VALUES ('Салат с овощами', 200, 1),
       ('Харчо', 300, 1),
       ('Бризоль из курицы', 350, 1),
       ('Салат Цезарь', 350, 2),
       ('Борщ с говядиной', 350, 2),
       ('Паста Арабьятта', 380, 2),
       ('Винегрет', 200, 3),
       ('Суп гороховый', 280, 3),
       ('Гречка по-купечески', 300, 3),
       ('Салат оливье', 250, 4),
       ('Уха по-фински', 400, 4),
       ('Котлеты пожарские', 320, 4),
       ('Морковь по-корейски', 250, 5),
       ('Том Ям', 330, 5),
       ('Вок терияки с курицей', 300, 5);

INSERT INTO VOTE (restaurant_id, user_id, vote_date, vote_time)
VALUES
--     (5, 1, current_date, '9:23'),
       (4, 2, current_date, '10:59'),
       (5, 3, current_date, '8:41'),
       (3, 2, '2023-12-31', '10:00'),
       (2, 1, '2023-12-31', '10:43'),
       (4, 3, '2023-12-31', '9:34');