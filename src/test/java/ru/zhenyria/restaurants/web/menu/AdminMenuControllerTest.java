package ru.zhenyria.restaurants.web.menu;

import org.hibernate.Hibernate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.zhenyria.restaurants.MenuTestData;
import ru.zhenyria.restaurants.model.Menu;
import ru.zhenyria.restaurants.model.Restaurant;
import ru.zhenyria.restaurants.service.MenuService;
import ru.zhenyria.restaurants.to.MenuTo;
import ru.zhenyria.restaurants.util.JsonUtil;
import ru.zhenyria.restaurants.util.exception.ErrorType;
import ru.zhenyria.restaurants.util.exception.NotFoundException;
import ru.zhenyria.restaurants.web.AbstractControllerTest;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.zhenyria.restaurants.DishTestData.FIRST_DISH_ID;
import static ru.zhenyria.restaurants.MenuTestData.*;
import static ru.zhenyria.restaurants.TestUtil.readFromJson;
import static ru.zhenyria.restaurants.TestUtil.userHttpBasic;
import static ru.zhenyria.restaurants.UserTestData.NOT_FOUND_ID;
import static ru.zhenyria.restaurants.UserTestData.admin;

public class AdminMenuControllerTest extends AbstractControllerTest {
    private static final String REST_URL = AdminMenuController.REST_URL + "/";
    private static final String DISHES_URL = AdminMenuController.DISHES_URL + "/";

    @Autowired
    private MenuService service;

    @Test
    void createWithLocation() throws Exception {
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(getNewTo())))
                .andExpect(status().isCreated());

        Menu created = readFromJson(action, Menu.class);
        Menu newMenu = getNew();
        int id = created.id();
        newMenu.setId(id);
        MENU_MATCHER.assertMatch(created, newMenu);
        MENU_MATCHER.assertMatch(service.get(created.id()), newMenu);
    }

    @Test
    void createInvalid() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL)
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(new MenuTo(null, null, null))))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(ErrorType.WRONG_DATA));
    }

    @Test
    void update() throws Exception {
        MenuTo updated = getUpdatedTo();
        perform(MockMvcRequestBuilders.put(REST_URL + FIRST_MENU_ID)
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());

        MENU_MATCHER_WITHOUT_DATE.assertMatch(unproxyMenu(service.get(updated.id())), getUpdated());
    }

    @Test
    void updateInvalid() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL + FIRST_MENU_ID)
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(new MenuTo(FIRST_MENU_ID, null, null))))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(ErrorType.WRONG_DATA));
    }

    @Test
    void addDish() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL + FIRST_MENU_ID + DISHES_URL + (FIRST_DISH_ID + 1))
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNoContent());
        MENU_MATCHER.assertMatch(service.get(FIRST_MENU_ID), MenuTestData.getWithAddedDish());
    }

    @Test
    void addDishToNotFound() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL + NOT_FOUND_ID + DISHES_URL + FIRST_DISH_ID)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(errorType(ErrorType.NOT_FOUND));
    }

    @Test
    void addNotFoundDish() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL + FIRST_MENU_ID + DISHES_URL + NOT_FOUND_ID)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(errorType(ErrorType.NOT_FOUND));
    }

    @Test
    void deleteDish() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + FIRST_MENU_ID + DISHES_URL + FIRST_DISH_ID)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNoContent());
        MENU_MATCHER.assertMatch(service.get(FIRST_MENU_ID), MenuTestData.getWithoutDeletedDish());
    }

    @Test
    void deleteDishFromNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + NOT_FOUND_ID + DISHES_URL + FIRST_DISH_ID)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(errorType(ErrorType.NOT_FOUND));
    }

    @Test
    void deleteNotFoundDish() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + FIRST_MENU_ID + DISHES_URL + NOT_FOUND_ID)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(errorType(ErrorType.NOT_FOUND));
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + FIRST_MENU_ID)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> service.get(FIRST_MENU_ID));
    }

    @Test
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + NOT_FOUND_ID)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(errorType(ErrorType.NOT_FOUND));
    }

    // Unproxy is used because MenuService#update use menu and restaurant references in one transaction with test
    // https://stackoverflow.com/questions/58509408/why-findbyid-returns-proxy-after-calling-getone-on-same-entity
    private Menu unproxyMenu(Menu menu) {
        menu.setRestaurant((Restaurant) Hibernate.unproxy(menu.getRestaurant()));
        return (Menu) Hibernate.unproxy(menu);
    }
}