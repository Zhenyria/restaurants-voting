package ru.zhenyria.restaurants.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.zhenyria.restaurants.model.Menu;
import ru.zhenyria.restaurants.util.exception.NotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.zhenyria.restaurants.MenuTestData.*;
import static ru.zhenyria.restaurants.RestaurantTestData.FIRST_RESTAURANT_ID;
import static ru.zhenyria.restaurants.RestaurantTestData.restaurant3;
import static ru.zhenyria.restaurants.UserTestData.NOT_FOUND_ID;

public class MenuServiceTest extends AbstractServiceTest {

    @Autowired
    private MenuService service;

    @Test
    void create() {
        Menu created = getNew();
        Menu newMenu = service.create(created);
        int id = newMenu.id();
        created.setId(id);
        MENU_MATCHER.assertMatch(newMenu, created);
        MENU_MATCHER.assertMatch(service.get(id), created);
    }

    @Test
    void get() {
        Menu menu = service.get(FIRST_MENU_ID);
        MENU_MATCHER.assertMatch(menu, menu1);
    }

    @Test
    void getNotExist() {
        assertThrows(RuntimeException.class, () -> service.get(NOT_FOUND_ID));
    }

    @Test
    void getActual() {
        Menu menu = service.getActual(restaurant3.id());
        MENU_MATCHER.assertMatch(menu, actualMenu2);
    }

    @Test
    void getForRestaurantByDate() {
        Menu menu = service.getForRestaurantByDate(FIRST_RESTAURANT_ID, DATE_12_01);
        MENU_MATCHER.assertMatch(menu, menu1);
    }

    @Test
    void getAllActual() {
        MENU_MATCHER.assertMatch(service.getAllActual(), List.of(actualMenu2, actualMenu1));
    }

    @Test
    void getAllByDate() {
        MENU_MATCHER.assertMatch(service.getAllByDate(DATE_12_01), List.of(menu1, menu6, menu4));
    }

    @Test
    void getAll() {
        MENU_MATCHER.assertMatch(service.getAll(),
                List.of(actualMenu2, actualMenu1, yesterdayMenu1, yesterdayMenu3, yesterdayMenu2,
                        menu3, menu2, menu7, menu5, menu1, menu6, menu4));
    }

    @Test
    void getAllForRestaurant() {
        MENU_MATCHER.assertMatch(service.getAllForRestaurant(FIRST_RESTAURANT_ID), List.of(yesterdayMenu1, menu3, menu2, menu1));
        MENU_MATCHER.assertMatch(service.getAllForRestaurant(FIRST_RESTAURANT_ID + 1), List.of(actualMenu1, yesterdayMenu2, menu5, menu4));
        MENU_MATCHER.assertMatch(service.getAllForRestaurant(FIRST_RESTAURANT_ID + 2), List.of(actualMenu2, yesterdayMenu3, menu7, menu6));
    }

    @Test
    void update() {
        service.update(getUpdated());
        MENU_MATCHER.assertMatch(service.get(FIRST_MENU_ID), getUpdated());
    }

    @Test
    void delete() {
        service.delete(FIRST_MENU_ID);
        assertThrows(NotFoundException.class, () -> service.get(FIRST_MENU_ID));
    }

    @Test
    void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND_ID));
    }
}
