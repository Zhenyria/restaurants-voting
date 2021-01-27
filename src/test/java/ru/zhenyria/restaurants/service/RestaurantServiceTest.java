package ru.zhenyria.restaurants.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.TransactionSystemException;
import ru.zhenyria.restaurants.MenuTestData;
import ru.zhenyria.restaurants.model.Restaurant;
import ru.zhenyria.restaurants.util.exception.NotFoundException;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.zhenyria.restaurants.RestaurantTestData.*;
import static ru.zhenyria.restaurants.UserTestData.NOT_FOUND_ID;

class RestaurantServiceTest extends AbstractServiceTest {

    @Autowired
    private RestaurantService service;

    @Test
    void create() {
        Restaurant created = service.create(getNew());
        int id = created.id();
        Restaurant newRestaurant = getNew();
        newRestaurant.setId(id);
        RESTAURANT_MATCHER.assertMatch(created, newRestaurant);
        RESTAURANT_MATCHER.assertMatch(service.get(id), newRestaurant);
    }

    @Test
    void createDuplicateName() {
        Restaurant restaurant = new Restaurant(restaurant1.getName());
        assertThrows(DataAccessException.class, () -> service.create(restaurant));
    }

    @Test
    void createWithException() {
        validateRootCause(() -> service.create(new Restaurant("  ")), ConstraintViolationException.class);
    }

    @Test
    void get() {
        Restaurant restaurant = service.get(FIRST_RESTAURANT_ID);
        RESTAURANT_MATCHER.assertMatch(restaurant, restaurant1);
    }

    @Test
    void getNotExist() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND_ID));
    }

    @Test
    void getAllWithActualMenu() {
        RESTAURANT_MATCHER.assertMatch(service.getAllWithActualMenu(), List.of(restaurant3, restaurant2));
    }

    @Test
    void getAllWithoutActualMenu() {
        RESTAURANT_MATCHER.assertMatch(service.getAllWithoutActualMenu(), restaurant1);
    }

    @Test
    void getAll() {
        RESTAURANT_MATCHER.assertMatch(service.getAll(), restaurants);
    }

    @Test
    void update() {
        Restaurant updated = getUpdated();
        service.update(updated);
        RESTAURANT_MATCHER.assertMatch(service.get(updated.id()), updated);
    }

    @Test
    void updateInvalid() {
        Restaurant updated = getUpdated();
        updated.setName(null);
        assertThrows(TransactionSystemException.class, () -> service.update(updated));
    }

    @Test
    void getWinning() {
        RESTAURANT_MATCHER.assertMatch(service.getWinning(), restaurant2);
    }

    @Test
    void getWinner() {
        RESTAURANT_MATCHER.assertMatch(service.getWinnerByDate(MenuTestData.DATE_12_01), restaurant1);
    }

    @Test
    void getWinnerByNullDate() {
        RESTAURANT_MATCHER.assertMatch(service.getWinner(null), restaurant2);
    }

    @Test
    void getWinnerByNotYetArrivedDate() {
        assertThrows(IllegalArgumentException.class, () -> service.getWinner(LocalDate.now()));
    }

    @Test
    void delete() {
        service.delete(FIRST_RESTAURANT_ID);
        RESTAURANT_MATCHER.assertMatch(service.getAll(), restaurant3, restaurant2);
    }

    @Test
    void deleteNotExist() {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND_ID));
    }
}
