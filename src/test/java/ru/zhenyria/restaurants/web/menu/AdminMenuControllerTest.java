package ru.zhenyria.restaurants.web.menu;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.zhenyria.restaurants.model.Menu;
import ru.zhenyria.restaurants.service.MenuService;
import ru.zhenyria.restaurants.util.JsonUtil;
import ru.zhenyria.restaurants.util.exception.ErrorType;
import ru.zhenyria.restaurants.util.exception.NotFoundException;
import ru.zhenyria.restaurants.web.AbstractControllerTest;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.zhenyria.restaurants.MenuTestData.*;
import static ru.zhenyria.restaurants.TestUtil.readFromJson;
import static ru.zhenyria.restaurants.TestUtil.userHttpBasic;
import static ru.zhenyria.restaurants.UserTestData.NOT_FOUND_ID;
import static ru.zhenyria.restaurants.UserTestData.admin;

public class AdminMenuControllerTest extends AbstractControllerTest {
    private static final String REST_URL = AdminMenuController.REST_URL + "/";

    @Autowired
    private MenuService service;

    @Test
    void createWithLocation() throws Exception {
        Menu menu = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(menu)))
                .andExpect(status().isCreated());

        Menu created = readFromJson(action, Menu.class);
        int id = created.id();
        menu.setId(id);
        MENU_MATCHER.assertMatch(created, menu);
        MENU_MATCHER.assertMatch(service.get(created.id()), menu);
    }

    @Test
    void createInvalid() throws Exception {
        Menu menu = new Menu(null, null, Collections.emptyList());
        perform(MockMvcRequestBuilders.post(REST_URL)
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(menu)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(ErrorType.WRONG_DATA));
    }

    @Test
    void update() throws Exception {
        Menu updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + "/" + FIRST_MENU_ID)
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());

        MENU_MATCHER.assertMatch(service.get(updated.id()), getUpdated());
    }

    @Test
    void updateInvalid() throws Exception {
        Menu updated = new Menu(FIRST_MENU_ID, null, null, Collections.emptyList());
        perform(MockMvcRequestBuilders.put(REST_URL + "/" + FIRST_MENU_ID)
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(ErrorType.WRONG_DATA));
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
}