# Restaurants Voting System [![Codacy Badge](https://app.codacy.com/project/badge/Grade/09c3308c5eae4f169e3ac884800e21cf)](https://www.codacy.com/gh/Zhenyria/restaurantsVotingSystem/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=Zhenyria/restaurantsVotingSystem&amp;utm_campaign=Badge_Grade) [![Build Status](https://travis-ci.org/Zhenyria/restaurantsVotingSystem.svg?branch=master)](https://travis-ci.org/Zhenyria/restaurantsVotingSystem)

REST-service for organizing a restaurant voting system. In app exist two types of users: ADMIN and USER. Admin can create restaurants, menu and dishes. User can vote for the liked restaurant or re-vote for other restaurant. Re-votings deadline is end at the eleven o'clock. At 00:00 will be selected winner.

**Stack of used technologies:**

- Spring Framework
- Spring MVC
- Spring Security
- Spring Data JPA
- Hibernate
- HSQLDB
- JUnit 5
- Jackson
- Cargo plugin _(you can run the application without any prior configuration)_

### Start the app
For start the app you can use Cargo plugin. Do the following after build the app:

`mvn clean package -DskipTests=true org.codehaus.cargo:cargo-maven2-plugin:1.8.2:run`

## REST-API
**WARNING:**
- Sequence in DB start with 100000
- Use date in path in format yyyy-MM-dd

### USERS
##### *FOR UNAUTHORIZED*
| description |method| curl |
|--|:--:|--|
| Register new user |POST|`curl --location --request POST 'http://localhost:8080/restaurants/rest/profile/register' --header 'Content-Type: application/json' --data-raw '{"name": "Kolya","email": "kolya@mail.ru","password": "password"}'`|
##### *FOR USER*
| description |method| curl |
|--|:--:|--|
| Get current user |GET|`curl --location --request GET 'http://localhost:8080/restaurants/rest/profile' --user piter@gmail.com:password`|
| Update current user |PUT|`curl --location --request PUT 'http://localhost:8080/restaurants/rest/profile' --header 'Content-Type: application/json' --data-raw '{"id": 100000,"name": "Zhora","email": "pink@mail.ru","password": "abrakadabra"}' --user piter@gmail.com:password`|
| Delete current user |DELETE|`curl --location --request DELETE 'http://localhost:8080/restaurants/rest/profile' --user piter@gmail.com:password`|
##### *FOR ADMIN*
| description |method| curl |
|--|:--:|--|
| Create new user |POST|`curl --location --request POST 'http://localhost:8080/restaurants/rest/admin/users' --header 'Content-Type: application/json' --data-raw '{"id": null,"name": "Zhora","email": "pink@mail.ru","password": "password","roles": ["USER"]}' --user admin@gmail.com:admin`|
| Get user by id |GET|`curl --location --request GET 'http://localhost:8080/restaurants/rest/admin/users/100000' --user admin@gmail.com:admin`|
| Get all users |GET|`curl --location --request GET 'http://localhost:8080/restaurants/rest/admin/users' --user admin@gmail.com:admin`|
| Update user |PUT|`curl --location --request PUT 'http://localhost:8080/restaurants/rest/admin/users/100000' --header 'Content-Type: application/json' --data-raw '{"id": 100000,"name": "Zhora","email": "pink@mail.ru","password": "abrakadabra"}' --user admin@gmail.com:admin`|
| Delete user |DELETE|`curl --location --request DELETE 'http://localhost:8080/restaurants/rest/admin/users/100000' --user admin@gmail.com:admin`|
### RESTAURANTS
##### *FOR USER*
| description |method| curl |
|--|:--:|--|
| Get by id |GET|`curl --location --request GET 'http://localhost:8080/restaurants/rest/restaurants/100004' --user piter@gmail.com:password`|
| Get winner _(can used with param "date")_ |GET|`curl --location --request GET 'http://localhost:8080/restaurants/rest/restaurants/winner?date=2020-12-01' --user piter@gmail.com:password`|
| Get winning _(return restaurant with the must chance of winning)_ |GET|`curl --location --request GET 'http://localhost:8080/restaurants/rest/restaurants/winning' --user piter@gmail.com:password`|
| Get all restaurants, which have actual menu |GET|`curl --location --request GET 'http://localhost:8080/restaurants/rest/restaurants' --user piter@gmail.com:password`|
##### *FOR ADMIN*
| description |method| curl |
|--|:--:|--|
| Create new restaurant |POST|`curl --location --request POST 'http://localhost:8080/restaurants/rest/admin/restaurants' --header 'Content-Type: application/json' --data-raw '{"id":null,"name":"Golden apple"}' --user admin@gmail.com:admin`|
| Get all |GET|`curl --location --request GET 'http://localhost:8080/restaurants/rest/admin/restaurants' --user admin@gmail.com:admin`|
| Get all, which have not actual menu |GET|`curl --location --request GET 'http://localhost:8080/restaurants/rest/admin/restaurants/without_menu' --user admin@gmail.com:admin`|
| Rename restaurant |PUT|`curl --location --request PUT 'http://localhost:8080/restaurants/rest/admin/restaurants/100004' --header 'Content-Type: application/json' --data-raw '"Golden"' --user admin@gmail.com:admin`|
| Delete restaurant |DELETE|`curl --location --request DELETE 'http://localhost:8080/restaurants/rest/admin/restaurants/100004' --user admin@gmail.com:admin`|
### MENUS
##### *FOR USER*
| description |method| curl |
|--|:--:|--|
| Get by id |GET|`curl --location --request GET 'http://localhost:8080/restaurants/rest/menus/100007' --user piter@gmail.com:password`|
| Get actual for restaurant |GET|`curl --location --request GET 'http://localhost:8080/restaurants/rest/restaurants/100006/menus/actual' --user piter@gmail.com:password`|
| Get all actual |GET|`curl --location --request GET 'http://localhost:8080/restaurants/rest/menus/actual' --user piter@gmail.com:password`|
| Get all _(can used with param "date" and "restaurantId")_ |GET|`curl --location --request GET 'http://localhost:8080/restaurants/rest/profile/restaurants/menus?date=2020-12-01&restaurantId=100004' --user piter@gmail.com:password`|
##### *FOR ADMIN*
| description |method| curl |
|--|:--:|--|
| Create new menu |POST|`curl --location --request POST 'http://localhost:8080/restaurants/rest/admin/menus' --header 'Content-Type: application/json' --data-raw '{"id": null,"restaurant": 100004,"dishes": [100017, 100018, 100019]}' --user admin@gmail.com:admin`|
| Update menu |PUT|`curl --location --request PUT 'http://localhost:8080/restaurants/rest/admin/menus/100007' --header 'Content-Type: application/json' --data-raw '{"id": 100007,"restaurantId": 100004,"dishes": [100017, 100018, 100019]}' --user admin@gmail.com:admin`|
| Add dish to menu |PUT|`curl --location --request PUT 'http://localhost:8080/restaurants/rest/admin/menus/100007/dishes/100026' --user admin@gmail.com:admin`|
| Delete dish from menu |DELETE|`curl --location --request DELETE 'http://localhost:8080/restaurants/rest/admin/menus/100007/dishes/100025' --user admin@gmail.com:admin`|
| Delete menu |DELETE|`curl --location --request DELETE 'http://localhost:8080/restaurants/rest/admin/menus/100016' --user admin@gmail.com:admin`|
### DISHES
##### *FOR ADMIN*
| description |method| curl |
|--|:--:|--|
| Create new dish |POST|`curl --location --request POST 'http://localhost:8080/restaurants/rest/admin/dishes' --header 'Content-Type: application/json' --data-raw '{"id":null,"name":"Cheese cake","price":56 }' --user admin@gmail.com:admin`|
| Get by id |GET|`curl --location --request GET 'http://localhost:8080/restaurants/rest/admin/dishes/100016' --user admin@gmail.com:admin`|
| Get all |GET|`curl --location --request GET 'http://localhost:8080/restaurants/rest/admin/dishes' --user admin@gmail.com:admin`|
| Update dish |PUT|`curl --location --request PUT 'http://localhost:8080/restaurants/rest/admin/dishes/100016' --header 'Content-Type: application/json' --data-raw '{"id":100016,"name":"Cheese cake","price":56 }' --user admin@gmail.com:admin`|
| Delete dish |DELETE|`curl --location --request DELETE 'http://localhost:8080/restaurants/rest/admin/dishes/100026' --user admin@gmail.com:admin`|
### VOTES
| description |method| curl |
|--|:--:|--|
| Get votes count of restaurant _(can used with param "date")_ |GET|`curl --location --request GET 'http://localhost:8080/restaurants/rest/restaurants/100004/votes?date=2020-12-01' --user piter@gmail.com:password`|
| Vote |POST|`curl --location --request POST 'http://localhost:8080/restaurants/rest/restaurants/100004/votes' --user piter@gmail.com:password`|