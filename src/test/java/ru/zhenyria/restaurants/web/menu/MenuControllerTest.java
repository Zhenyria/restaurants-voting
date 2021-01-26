package ru.zhenyria.restaurants.web.menu;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.zhenyria.restaurants.util.exception.ErrorType;
import ru.zhenyria.restaurants.web.AbstractControllerTest;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.zhenyria.restaurants.MenuTestData.*;
import static ru.zhenyria.restaurants.RestaurantTestData.FIRST_RESTAURANT_ID;
import static ru.zhenyria.restaurants.RestaurantTestData.restaurant3;
import static ru.zhenyria.restaurants.TestUtil.userHttpBasic;
import static ru.zhenyria.restaurants.UserTestData.NOT_FOUND_ID;
import static ru.zhenyria.restaurants.UserTestData.user1;

public class MenuControllerTest extends AbstractControllerTest {
    private static final String REST_URL = MenuController.REST_URL + MenuController.MENUS_URL + "/";

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + FIRST_MENU_ID)
                .with(userHttpBasic(user1)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_MATCHER.contentJson(menu1));
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + NOT_FOUND_ID)
                .with(userHttpBasic(user1)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(errorType(ErrorType.NOT_FOUND));
    }

    @Test
    void getActual() throws Exception {
        perform(MockMvcRequestBuilders.get(
                MenuController.REST_URL + "/restaurants/" + restaurant3.id() + "/menus/actual")
                .with(userHttpBasic(user1)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_MATCHER.contentJson(actualMenu2));
    }

    @Test
    void getAllActual() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "actual")
                .with(userHttpBasic(user1)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_MATCHER.contentJson(List.of(actualMenu2, actualMenu1)));
    }

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(
                REST_URL + "?date=" + DATE_12_01 + "&restaurantId=" + FIRST_RESTAURANT_ID)
                .with(userHttpBasic(user1)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_MATCHER.contentJson(List.of(menu1)));
    }

    @Test
    void getAllWithNullRestaurantId() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "?date=" + DATE_12_01)
                .with(userHttpBasic(user1)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_MATCHER.contentJson(List.of(menu1, menu6, menu4)));
    }

    @Test
    void getAllWithNullDate() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "?restaurantId=" + FIRST_RESTAURANT_ID)
                .with(userHttpBasic(user1)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_MATCHER.contentJson(List.of(yesterdayMenu1, menu3, menu2, menu1)));
    }

    @Test
    void getAllWithNullDateAndNullRestaurantId() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(user1)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_MATCHER.contentJson(List.of(actualMenu2, actualMenu1,
                        yesterdayMenu1, yesterdayMenu3, yesterdayMenu2,
                        menu3, menu2, menu7, menu5, menu1, menu6, menu4)));
    }
}
