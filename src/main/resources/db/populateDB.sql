DELETE FROM MENU_DISHES;
DELETE FROM DISHES;
DELETE FROM VOTES;
DELETE FROM MENUS;
DELETE FROM RESTAURANTS;
DELETE FROM USER_ROLES;
DELETE FROM USERS;
ALTER SEQUENCE GLOBAL_SEQ RESTART WITH 100000;

INSERT INTO USERS (NAME, PASSWORD, EMAIL)
VALUES ('Piter', 'password', 'piter@gmail.com'),
       ('Nikolas', 'password', 'nikolas@gmail.com'),
       ('Petr', 'password', 'petr@mail.ru'),
       ('Admin', 'admin', 'admin@gmail.com');

INSERT INTO USER_ROLES (USER_ID, ROLE)
VALUES (100000, 'USER'),
       (100001, 'USER'),
       (100002, 'USER'),
       (100003, 'USER'),
       (100003, 'ADMIN');

INSERT INTO RESTAURANTS (NAME)
VALUES ('Goldy'),
       ('Siberian eggs'),
       ('Moscow Palace 1992');

INSERT INTO MENUS (RESTAURANT_ID, DATE)
VALUES (100004, '2020-12-01'),
       (100004, '2020-12-02'),
       (100004, '2020-12-03'),
       (100005, '2020-12-01'),
       (100005, '2020-12-02'),
       (100005, '2020-12-03'),
       (100006, '2020-12-01'),
       (100006, '2020-12-02'),
       (100006, '2020-12-03');

INSERT INTO VOTES (USER_ID, MENU_ID)
VALUES (100000, 100007),
       (100000, 100011),
       (100000, 100012),
       (100001, 100007),
       (100001, 100014),
       (100001, 100015),
       (100002, 100010),
       (100002, 100011),
       (100002, 100009);

INSERT INTO DISHES (NAME, PRICE)
VALUES ('Beef', 154),
       ('Cola', 46),
       ('Zero Cola', 47),
       ('Fish soup', 118),
       ('Eggs', 24),
       ('Coffee', 36),
       ('Meat', 118),
       ('Beer', 52),
       ('Pina Coladas', 106),
       ('Corny flakes', 60),
       ('Hamburger', 103);

INSERT INTO MENU_DISHES (DISH_ID, MENU_ID)
VALUES (100016, 100007),
       (100016, 100010),
       (100016, 100011),
       (100017, 100015),
       (100017, 100008),
       (100018, 100013),
       (100018, 100007),
       (100018, 100009),
       (100018, 100014),
       (100019, 100007),
       (100019, 100008),
       (100019, 100009),
       (100019, 100010),
       (100019, 100011),
       (100019, 100012),
       (100019, 100013),
       (100019, 100015),
       (100020, 100009),
       (100020, 100013),
       (100020, 100010),
       (100020, 100012),
       (100021, 100013),
       (100021, 100014),
       (100021, 100015),
       (100022, 100011),
       (100022, 100010),
       (100022, 100012),
       (100022, 100014),
       (100023, 100007),
       (100023, 100009),
       (100023, 100011),
       (100024, 100013),
       (100024, 100014),
       (100024, 100015),
       (100025, 100010),
       (100025, 100007),
       (100026, 100011),
       (100026, 100012),
       (100026, 100013);