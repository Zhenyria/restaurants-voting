package ru.zhenyria.restaurants.web.restaurant;

import org.hibernate.Hibernate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.zhenyria.restaurants.model.Restaurant;
import ru.zhenyria.restaurants.service.RestaurantService;
import ru.zhenyria.restaurants.util.JsonUtil;
import ru.zhenyria.restaurants.util.exception.ErrorType;
import ru.zhenyria.restaurants.util.exception.NotFoundException;
import ru.zhenyria.restaurants.web.AbstractControllerTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.zhenyria.restaurants.RestaurantTestData.*;
import static ru.zhenyria.restaurants.TestUtil.readFromJson;
import static ru.zhenyria.restaurants.TestUtil.userHttpBasic;
import static ru.zhenyria.restaurants.UserTestData.NOT_FOUND_ID;
import static ru.zhenyria.restaurants.UserTestData.admin;
import static ru.zhenyria.restaurants.web.ExceptionInfoHandler.EXCEPTION_DUPLICATE_NAME;

public class AdminRestaurantControllerTest extends AbstractControllerTest {
    private static final String REST_URL = AdminRestaurantController.REST_URL + "/";

    @Autowired
    private RestaurantService service;

    @Test
    void createWithLocation() throws Exception {
        Restaurant restaurant = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(restaurant))
                .with(userHttpBasic(admin)))
                .andExpect(status().isCreated());

        Restaurant created = readFromJson(action, Restaurant.class);
        int id = created.id();
        restaurant.setId(id);
        RESTAURANT_MATCHER.assertMatch(created, restaurant);
        RESTAURANT_MATCHER.assertMatch(service.get(id), restaurant);
    }

    @Test
    void createInvalid() throws Exception {
        Restaurant restaurant = new Restaurant("  ");
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(restaurant))
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void createWithDuplicateName() throws Exception {
        Restaurant restaurant = new Restaurant(restaurant1.getName());
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(restaurant))
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(detailMessage(EXCEPTION_DUPLICATE_NAME));
    }

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(restaurants));
    }

    @Test
    void getAllWithoutActualMenu() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "without_menu")
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(List.of(restaurant1)));
    }

    @Test
    void rename() throws Exception {
        String newName = "Milk bar";
        perform(MockMvcRequestBuilders.put(REST_URL + FIRST_RESTAURANT_ID)
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newName)))
                .andDo(print())
                .andExpect(status().isNoContent());

        Restaurant restaurant = new Restaurant(restaurant1);
        restaurant.setName(newName);

        // Unproxy is used because RestaurantService#update use restaurant reference in one transaction with test
        // https://stackoverflow.com/questions/58509408/why-findbyid-returns-proxy-after-calling-getone-on-same-entity
        RESTAURANT_MATCHER.assertMatch((Restaurant) Hibernate.unproxy(service.get(FIRST_RESTAURANT_ID)), restaurant);
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void renameWithDuplicateName() throws Exception {
        String newName = restaurant2.getName();
        perform(MockMvcRequestBuilders.put(REST_URL + FIRST_RESTAURANT_ID)
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newName)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(errorType(ErrorType.WRONG_DATA))
                .andExpect(detailMessage(EXCEPTION_DUPLICATE_NAME));
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + FIRST_RESTAURANT_ID)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> service.get(FIRST_RESTAURANT_ID));
    }

    @Test
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + NOT_FOUND_ID)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(errorType(ErrorType.NOT_FOUND));
    }
}
