# Restaurants Voting System
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

### REST-API
**WARNING:**
- Sequence in DB start with 100000
- Use date in path in format yyyy-MM-dd

#### USERS
##### *FOR UNAUTHORIZED*
| description |method| curl |
|--|--|--|
| Register new user |POST|`curl --location --request POST 'http://localhost:8080/restaurants/rest/profile/register' --header 'Content-Type: application/json' --data-raw '{"name": "Kolya","email": "kolya@mail.ru","password": "password"}'`|
##### *FOR USER*
| description |method| curl |
|--|--|--|
| Get current user |GET|`curl --location --request GET 'http://localhost:8080/restaurants/rest/profile' --user piter@gmail.com:password`|
| Update current user |PUT|`curl --location --request PUT 'http://localhost:8080/restaurants/rest/profile' --header 'Content-Type: application/json' --data-raw '{"id": 100000,"name": "Zhora","email": "pink@mail.ru","password": "abrakadabra"}' --user piter@gmail.com:password`|
| Delete current user |DELETE|`curl --location --request DELETE 'http://localhost:8080/restaurants/rest/profile' --user piter@gmail.com:password`|
##### *FOR ADMIN*
| description |method| curl |
|--|--|--|
| Create new user |POST|`curl --location --request POST 'http://localhost:8080/restaurants/rest/admin/users' --header 'Content-Type: application/json' --data-raw '{"id": null,"name": "Zhora","email": "pink@mail.ru","password": "password","roles": ["USER"]}' --user admin@gmail.com:password`|
| Get user by id |GET|`curl --location --request GET 'http://localhost:8080/restaurants/rest/admin/users/100000' --user admin@gmail.com:password`|
| Get all users |GET|`curl --location --request GET 'http://localhost:8080/restaurants/rest/admin/users' --user admin@gmail.com:password`|
| Update user |PUT|`curl --location --request PUT 'http://localhost:8080/restaurants/rest/admin/users' --header 'Content-Type: application/json' --data-raw '{"id": 100000,"name": "Zhora","email": "pink@mail.ru","password": "abrakadabra"}' --user admin@gmail.com:password`|
| Delete user |DELETE|`curl --location --request DELETE 'http://localhost:8080/restaurants/rest/admin/users/100000' --user admin@gmail.com:password`|
#### RESTAURANTS
##### *FOR USER*
| description |method| curl |
|--|--|--|
| Get by id |GET|`curl --location --request GET 'http://localhost:8080/restaurants/rest/profile/restaurants/100004' --user piter@gmail.com:password`|
| Get winner |GET|`curl --location --request GET 'http://localhost:8080/restaurants/rest/profile/restaurants/winner' --user piter@gmail.com:password`|
| Get winning |GET|`curl --location --request GET 'http://localhost:8080/restaurants/rest/profile/restaurants/winning' --user piter@gmail.com:password`|
| Get winner by date |GET|`curl --location --request GET 'http://localhost:8080/restaurants/rest/profile/restaurants/winner/2020-12-01' --user piter@gmail.com:password`|
| Get all restaurants, which have actual menu |GET|`curl --location --request GET 'http://localhost:8080/restaurants/rest/profile/restaurants' --user piter@gmail.com:password`|
| Get votes count of restaurant |GET|`curl --location --request GET 'http://localhost:8080/restaurants/rest/profile/restaurants/100004/rating' --user piter@gmail.com:password`|
| Get votes count of restaurant by date |GET|`curl --location --request GET 'http://localhost:8080/restaurants/rest/profile/restaurants/100004/rating/2020-12-01' --user piter@gmail.com:password`|
| Vote |POST|`curl --location --request POST 'http://localhost:8080/restaurants/rest/profile/restaurants/100004/vote' --user piter@gmail.com:password`|
##### *FOR ADMIN*
| description |method| curl |
|--|--|--|
| Create new restaurant |POST|`curl --location --request POST 'http://localhost:8080/restaurants/rest/admin/restaurants' --header 'Content-Type: application/json' --data-raw '{"id":null,"name":"Golden apple"}' --user admin@gmail.com:password`|
| Get all |GET|`curl --location --request GET 'http://localhost:8080/restaurants/rest/admin/restaurants' --user admin@gmail.com:password`|
| Get all, which have not actual menu |GET|`curl --location --request GET 'http://localhost:8080/restaurants/rest/admin/restaurants/without_menu' --user admin@gmail.com:password`|
| Rename restaurant |PUT|`curl --location --request PUT 'http://localhost:8080/restaurants/rest/admin/restaurants/100004' --header 'Content-Type: application/json' --data-raw '"\"Golden\""' --user admin@gmail.com:password`|
| Delete restaurant |DELETE|`curl --location --request DELETE 'http://localhost:8080/restaurants/rest/admin/restaurants/100004' --user admin@gmail.com:password`|
#### MENUS
##### *FOR USER*
| description |method| curl |
|--|--|--|
| Get by id |GET|`curl --location --request GET 'http://localhost:8080/restaurants/rest/profile/restaurants/menus/100007' --user piter@gmail.com:password`|
| Get actual for restaurant |GET|`curl --location --request GET 'http://localhost:8080/restaurants/rest/profile/restaurants/100006/menus/actual' --user piter@gmail.com:password`|
| Get for restaurant by date |GET|`curl --location --request GET 'http://localhost:8080/restaurants/rest/profile/restaurants/100006/menus/2020-12-01' --user piter@gmail.com:password`|
| Get all actual |GET|`curl --location --request GET 'http://localhost:8080/restaurants/rest/profile/restaurants/menus/actual' --user piter@gmail.com:password`|
| Get all by date |GET|`curl --location --request GET 'http://localhost:8080/restaurants/rest/profile/restaurants/menus/actual/2020-12-01' --user piter@gmail.com:password`|
| Get all for restaurant |GET|`curl --location --request GET 'http://localhost:8080/restaurants/rest/profile/restaurants/100006/menus' --user piter@gmail.com:password`|
| Get all |GET|`curl --location --request GET 'http://localhost:8080/restaurants/rest/profile/restaurants/menus' --user piter@gmail.com:password`|
##### *FOR ADMIN*
| description |method| curl |
|--|--|--|
| Create new menu |POST|`curl --location --request POST 'http://localhost:8080/restaurants/rest/admin/restaurants/menus' --header 'Content-Type: application/json' --data-raw '{"id": null,"restaurant": {"id": 100004,"name": "Goldy"},"dishes": [{"id": 100017,"name": "Cola","price": 46 }, {"id": 100018,"name": "Zero cola","price": 47 }, {"id": 100019,"name": "Fish soup","price": 118 }]}' --user admin@gmail.com:password`|
| Update menu |PUT|`curl --location --request PUT 'http://localhost:8080/restaurants/rest/admin/restaurants/menus' --header 'Content-Type: application/json' --data-raw '{"id": 100007,"restaurant": {"id": 100004,"name": "Goldy"},"dishes": [{"id": 100017,"name": "Cola","price": 46 }, {"id": 100018,"name": "Zero cola","price": 47 }, {"id": 100019,"name": "Fish soup","price": 118 }]}' --user admin@gmail.com:password`|
| Delete menu |DELETE|`curl --location --request DELETE 'http://localhost:8080/restaurants/rest/admin/restaurants/menus/100016' --user admin@gmail.com:password`|
#### DISHES
##### *FOR ADMIN*
| description |method| curl |
|--|--|--|
| Create new dish |POST|`curl --location --request POST 'http://localhost:8080/restaurants/rest/admin/restaurants/menus/dishes' --header 'Content-Type: application/json' --data-raw '{"id":null,"name":"Cheese cake","price":56 }' --user admin@gmail.com:password`|
| Get by id |GET|`curl --location --request GET 'http://localhost:8080/restaurants/rest/admin/restaurants/menus/dishes/100016' --user admin@gmail.com:password`|
| Get all |GET|`curl --location --request GET 'http://localhost:8080/restaurants/rest/admin/restaurants/menus/dishes' --user admin@gmail.com:password`|
| Update dish |PUT|`curl --location --request PUT 'http://localhost:8080/restaurants/rest/admin/restaurants/menus/dishes' --header 'Content-Type: application/json' --data-raw '{"id":100016,"name":"Cheese cake","price":56 }' --user admin@gmail.com:password`|
| Add dish to menu |PUT|`curl --location --request PUT 'http://localhost:8080/restaurants/rest/admin/restaurants/menus/100007/dishes/100026' --user admin@gmail.com:password`|
| Delete dish from menu |DELETE|`curl --location --request DELETE 'http://localhost:8080/restaurants/rest/admin/restaurants/menus/100007/dishes/100025' --user admin@gmail.com:password`|
| Delete dish |DELETE|`curl --location --request DELETE 'http://localhost:8080/restaurants/rest/admin/restaurants/menus/dishes/100026' --user admin@gmail.com:password`|