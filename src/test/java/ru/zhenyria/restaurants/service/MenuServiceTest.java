package ru.zhenyria.restaurants.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.zhenyria.restaurants.MenuTestData;
import ru.zhenyria.restaurants.RestaurantTestData;
import ru.zhenyria.restaurants.model.Menu;
import ru.zhenyria.restaurants.to.MenuTo;
import ru.zhenyria.restaurants.util.exception.NotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.zhenyria.restaurants.DishTestData.FIRST_DISH_ID;
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
    void getAllActual() {
        MENU_MATCHER.assertMatch(service.getAllActual(), List.of(actualMenu2, actualMenu1));
    }

    @Test
    void getAll() {
        List<Menu> menus = service.getAll(DATE_12_01, FIRST_RESTAURANT_ID);
        MENU_MATCHER.assertMatch(menus, List.of(menu1));
    }

    @Test
    void getAllWithNullRestaurantId() {
        MENU_MATCHER.assertMatch(service.getAll(DATE_12_01, null), List.of(menu1, menu6, menu4));
    }

    @Test
    void getAllWithNullDate() {
        MENU_MATCHER.assertMatch(service.getAll(null, FIRST_RESTAURANT_ID), List.of(yesterdayMenu1, menu3, menu2, menu1));
        MENU_MATCHER.assertMatch(service.getAll(null, FIRST_RESTAURANT_ID + 1), List.of(actualMenu1, yesterdayMenu2, menu5, menu4));
        MENU_MATCHER.assertMatch(service.getAll(null, FIRST_RESTAURANT_ID + 2), List.of(actualMenu2, yesterdayMenu3, menu7, menu6));
    }

    @Test
    void getAllWithNullDateAndNullRestaurantId() {
        MENU_MATCHER.assertMatch(service.getAll(null, null),
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
    void addDish() {
        service.addDish(FIRST_MENU_ID, FIRST_DISH_ID + 1);
        MENU_MATCHER.assertMatch(service.get(FIRST_MENU_ID), MenuTestData.getWithAddedDish());
    }

    @Test
    void deleteDish() {
        service.deleteDish(FIRST_MENU_ID, FIRST_DISH_ID);
        MENU_MATCHER.assertMatch(service.get(FIRST_MENU_ID), MenuTestData.getWithoutDeletedDish());
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
