package ru.zhenyria.restaurants.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.TransactionSystemException;
import ru.zhenyria.restaurants.model.Menu;
import ru.zhenyria.restaurants.to.MenuTo;

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
    void createDuplicateDate() {
        Menu created = getNew();
        created.setDate(DATE_12_03);
        assertThrows(DataAccessException.class, () -> service.create(created));
    }

    @Test
    void createFromTo() {
        int id = service.create(getNewTo()).id();
        Menu created = getNew();
        created.setId(id);
        MENU_MATCHER.assertMatch(service.get(id), created);
    }

    @Test
    void createDuplicateDateFromTo() {
        MenuTo newMenu = getNewTo();
        newMenu.setDate(DATE_12_03);
        assertThrows(DataAccessException.class, () -> service.create(newMenu));
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
                List.of(actualMenu2, actualMenu1, menu3, menu2, menu7, menu5, menu1, menu6, menu4));
    }

    @Test
    void getAllForRestaurant() {
        MENU_MATCHER.assertMatch(service.getAllForRestaurant(FIRST_RESTAURANT_ID), List.of(menu3, menu2, menu1));
        MENU_MATCHER.assertMatch(service.getAllForRestaurant(FIRST_RESTAURANT_ID + 1), List.of(actualMenu1, menu5, menu4));
        MENU_MATCHER.assertMatch(service.getAllForRestaurant(FIRST_RESTAURANT_ID + 2), List.of(actualMenu2, menu7, menu6));
    }

    @Test
    void update() {
        service.update(getUpdated());
        MENU_MATCHER.assertMatch(service.get(FIRST_MENU_ID), getUpdated());
    }

    @Test
    void updateInvalid() {
        Menu menu = getUpdated();
        menu.setDate(null);
        assertThrows(TransactionSystemException.class, () -> service.update(menu));
    }

    @Test
    void updateDuplicateDate() {
        Menu menu = getUpdated();
        menu.setDate(DATE_12_02);
        assertThrows(DataIntegrityViolationException.class, () -> service.update(menu));
    }

    @Test
    void updateTo() {
        service.update(getUpdatedTo());
        MENU_MATCHER.assertMatch(service.get(FIRST_MENU_ID), getUpdated());
    }

    @Test
    void updateToInvalid() {
        MenuTo menu = getUpdatedTo();
        menu.setDate(null);
        assertThrows(TransactionSystemException.class, () -> service.update(menu));
    }

    @Test
    void updateToDuplicateDate() {
        MenuTo menu = getUpdatedTo();
        menu.setDate(DATE_12_02);
        assertThrows(DataIntegrityViolationException.class, () -> service.update(menu));
    }

    @Test
    void delete() {
        service.delete(FIRST_MENU_ID);
        assertThrows(RuntimeException.class, () -> service.get(FIRST_MENU_ID));
    }

    @Test
    void deleteNotFound() {
        assertThrows(RuntimeException.class, () -> service.delete(NOT_FOUND_ID));
    }
}
