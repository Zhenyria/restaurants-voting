package ru.zhenyria.restaurants.web.dish;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.zhenyria.restaurants.MenuTestData;
import ru.zhenyria.restaurants.model.Dish;
import ru.zhenyria.restaurants.service.DishService;
import ru.zhenyria.restaurants.service.MenuService;
import ru.zhenyria.restaurants.util.JsonUtil;
import ru.zhenyria.restaurants.util.exception.ErrorType;
import ru.zhenyria.restaurants.util.exception.NotFoundException;
import ru.zhenyria.restaurants.web.AbstractControllerTest;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.zhenyria.restaurants.DishTestData.*;
import static ru.zhenyria.restaurants.MenuTestData.FIRST_MENU_ID;
import static ru.zhenyria.restaurants.MenuTestData.MENU_MATCHER;
import static ru.zhenyria.restaurants.TestUtil.readFromJson;
import static ru.zhenyria.restaurants.TestUtil.userHttpBasic;
import static ru.zhenyria.restaurants.UserTestData.NOT_FOUND_ID;
import static ru.zhenyria.restaurants.UserTestData.admin;
import static ru.zhenyria.restaurants.web.ExceptionInfoHandler.EXCEPTION_DUPLICATE_NAME_PRICE;
import static ru.zhenyria.restaurants.web.dish.DishController.DISHES_URL;

public class DishControllerTest extends AbstractControllerTest {
    private static final String REST_URL = DishController.REST_URL + "/";

    @Autowired
    private DishService service;

    @Autowired
    private MenuService menuService;

    @Test
    void createWithLocation() throws Exception {
        Dish newDish = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL + DISHES_URL)
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newDish)))
                .andDo(print())
                .andExpect(status().isCreated());

        Dish created = readFromJson(action, Dish.class);
        int id = created.id();
        newDish.setId(id);
        DISH_MATCHER.assertMatch(created, newDish);
        DISH_MATCHER.assertMatch(service.get(id), newDish);
    }

    @Test
    void createInvalid() throws Exception {
        Dish newDish = new Dish(null, "  ", 0);
        perform(MockMvcRequestBuilders.post(REST_URL + DISHES_URL)
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newDish)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(ErrorType.WRONG_DATA));
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void createWithDuplicateNameAndPrice() throws Exception {
        Dish newDish = new Dish(dish1);
        newDish.setId(null);
        perform(MockMvcRequestBuilders.post(REST_URL + DISHES_URL)
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newDish)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(ErrorType.WRONG_DATA))
                .andExpect(detailMessage(EXCEPTION_DUPLICATE_NAME_PRICE));
    }

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + DISHES_URL + "/" + FIRST_DISH_ID)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(dish1));
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + DISHES_URL + "/" + NOT_FOUND_ID)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(errorType(ErrorType.NOT_FOUND));
    }

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + DISHES_URL)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(dishes));
    }

    @Test
    void update() throws Exception {
        Dish updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + DISHES_URL)
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());

        DISH_MATCHER.assertMatch(service.get(updated.id()), getUpdated());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void updateInvalid() throws Exception {
        Dish dish = new Dish(FIRST_DISH_ID, "  ", 0);
        perform(MockMvcRequestBuilders.put(REST_URL + DISHES_URL)
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(dish)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(ErrorType.OPERATION_FAILED));
    }

    @Test
    void addToMenu() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL + FIRST_MENU_ID + DISHES_URL + "/" + (FIRST_DISH_ID + 1))
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNoContent());
        MENU_MATCHER.assertMatch(menuService.get(FIRST_MENU_ID), MenuTestData.getWithAddedDish());
    }

    @Test
    void addToNotFoundMenu() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL + NOT_FOUND_ID + DISHES_URL + "/" + FIRST_DISH_ID)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(ErrorType.WRONG_DATA));
    }

    @Test
    void deleteFromMenu() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + FIRST_MENU_ID + DISHES_URL + "/" + FIRST_DISH_ID)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNoContent());
        MENU_MATCHER.assertMatch(menuService.get(FIRST_MENU_ID), MenuTestData.getWithoutDeletedDish());
    }

    @Test
    void deleteNotFoundFromMenu() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + NOT_FOUND_ID + DISHES_URL + "/" + FIRST_DISH_ID)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(errorType(ErrorType.NOT_FOUND));
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + DISHES_URL + "/" + NOT_USING_DISH_ID)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> service.get(NOT_USING_DISH_ID));
    }

    @Test
    void deleteUsing() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + DISHES_URL + "/" + FIRST_DISH_ID)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(errorType(ErrorType.FORBIDDEN_OPERATION));
    }

    @Test
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + DISHES_URL + "/" + NOT_FOUND_ID)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(errorType(ErrorType.NOT_FOUND));
    }
}
