package ru.zhenyria.restaurants.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.zhenyria.restaurants.RestaurantTestData;
import ru.zhenyria.restaurants.model.Menu;
import ru.zhenyria.restaurants.to.MenuTo;
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
        Menu created = service.create(getNewTo());
        int id = created.id();
        Menu newMenu = getNew();
        newMenu.setId(id);
        MENU_MATCHER.assertMatch(created, newMenu);
        MENU_MATCHER.assertMatch(service.get(id), newMenu);
    }

    @Test
    void createInvalid() {
        assertThrows(NullPointerException.class,
                () -> service.create(new MenuTo(null, null, null)));
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
    void getByRestaurant() {
        List<Menu> menus = service.getByRestaurant(FIRST_RESTAURANT_ID, DATE_12_01);
        MENU_MATCHER.assertMatch(menus, List.of(menu1));
    }

    @Test
    void getByRestaurantWithNullDate() {
        MENU_MATCHER.assertMatch(service.getByRestaurant(FIRST_RESTAURANT_ID, null), List.of(yesterdayMenu1, menu3, menu2, menu1));
        MENU_MATCHER.assertMatch(service.getByRestaurant(FIRST_RESTAURANT_ID + 1, null), List.of(actualMenu1, yesterdayMenu2, menu5, menu4));
        MENU_MATCHER.assertMatch(service.getByRestaurant(FIRST_RESTAURANT_ID + 2, null), List.of(actualMenu2, yesterdayMenu3, menu7, menu6));
    }

    @Test
    void getAllActual() {
        MENU_MATCHER.assertMatch(service.getAllActual(), List.of(actualMenu2, actualMenu1));
    }

    @Test
    void getAll() {
        MENU_MATCHER.assertMatch(service.getAll(DATE_12_01), List.of(menu1, menu6, menu4));
    }

    @Test
    void getAllWithNullDate() {
        MENU_MATCHER.assertMatch(service.getAll(null),
                List.of(actualMenu2, actualMenu1, yesterdayMenu1, yesterdayMenu3, yesterdayMenu2,
                        menu3, menu2, menu7, menu5, menu1, menu6, menu4));
    }

    @Test
    void update() {
        service.update(getUpdatedTo());
        MENU_MATCHER.assertMatch(service.get(FIRST_MENU_ID), getUpdated());
    }

    @Test
    void updateWithOtherRestaurant() {
        MenuTo menu = getUpdatedTo();
        menu.setRestaurantId(RestaurantTestData.restaurant2.id());
        service.update(menu);
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
