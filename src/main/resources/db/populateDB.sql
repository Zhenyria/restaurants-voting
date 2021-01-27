DELETE
FROM menus_dishes;
DELETE
FROM dishes;
DELETE
FROM votes;
DELETE
FROM menus;
DELETE
FROM restaurants;
DELETE
FROM users_roles;
DELETE
FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (NAME, PASSWORD, EMAIL)
VALUES ('Piter', '{noop}password', 'piter@gmail.com'),
       ('Nikolas', '{noop}password', 'nikolas@gmail.com'),
       ('Petr', '{noop}password', 'petr@mail.ru'),
       ('Admin', '{noop}admin', 'admin@gmail.com');

INSERT INTO users_roles (USER_ID, ROLE)
VALUES (100000, 'USER'),
       (100001, 'USER'),
       (100002, 'USER'),
       (100003, 'USER'),
       (100003, 'ADMIN');

INSERT INTO restaurants (NAME)
VALUES ('Goldy'),
       ('Siberian eggs'),
       ('Moscow Palace 1992');

INSERT INTO menus (RESTAURANT_ID, DATE)
VALUES (100004, '2020-12-01'),
       (100004, '2020-12-02'),
       (100004, '2020-12-03'),
       (100005, '2020-12-01'),
       (100005, '2020-12-02'),
       (100006, '2020-12-01'),
       (100006, '2020-12-02');

INSERT INTO menus (RESTAURANT_ID)
VALUES (100005),
       (100006);

INSERT INTO votes (USER_ID, RESTAURANT_ID, DATE)
VALUES (100000, 100004, '2020-12-01'),
       (100000, 100005, '2020-12-02'),
       (100000, 100004, '2020-12-03'),
       (100001, 100004, '2020-12-01'),
       (100001, 100006, '2020-12-02'),
       (100002, 100005, '2020-12-01'),
       (100002, 100004, '2020-12-03'),
       (100002, 100006, '2020-12-02');

INSERT INTO votes (USER_ID, RESTAURANT_ID)
VALUES (100000, 100005),
       (100001, 100005),
       (100002, 100006);

INSERT INTO dishes (NAME, PRICE)
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
       ('Hamburger', 103),
       ('Sprite', 23);

INSERT INTO menus_dishes (DISH_ID, MENU_ID)
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

/* Test data for testing getWinner() */

INSERT INTO menus (RESTAURANT_ID, DATE)
VALUES (100004, DATEADD('day', -1, TODAY())),
       (100005, DATEADD('day', -1, TODAY())),
       (100006, DATEADD('day', -1, TODAY()));

INSERT INTO votes (USER_ID, RESTAURANT_ID, DATE)
VALUES (100000, 100004, DATEADD('day', -1, TODAY())),
       (100001, 100005, DATEADD('day', -1, TODAY())),
       (100002, 100005, DATEADD('day', -1, TODAY())),
       (100003, 100006, DATEADD('day', -1, TODAY()));