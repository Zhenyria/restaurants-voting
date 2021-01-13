package ru.zhenyria.restaurants.web.restaurant;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.zhenyria.restaurants.MenuTestData;
import ru.zhenyria.restaurants.service.RestaurantService;
import ru.zhenyria.restaurants.util.VoteUtil;
import ru.zhenyria.restaurants.util.exception.ErrorType;
import ru.zhenyria.restaurants.web.AbstractControllerTest;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.zhenyria.restaurants.RestaurantTestData.*;
import static ru.zhenyria.restaurants.TestUtil.userHttpBasic;
import static ru.zhenyria.restaurants.UserTestData.*;

public class RestaurantControllerTest extends AbstractControllerTest {
    private static final String REST_URL = RestaurantController.REST_URL + "/";

    @Autowired
    private RestaurantService service;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + FIRST_RESTAURANT_ID)
                .with(userHttpBasic(user1)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(restaurant1));
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
    void getWinner() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "winner?date=" + MenuTestData.DATE_12_01)
                .with(userHttpBasic(user1)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(restaurant1));
    }

    @Test
    void getWinnerByNullDate() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "winner")
                .with(userHttpBasic(user1)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(restaurant2));
    }

    @Test
    void getWinnerByIllegalDate() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "winner?date=1")
                .with(userHttpBasic(user1)))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(errorType(ErrorType.APP_ERROR));
    }

    @Test
    void getWinnerByNotYetArrivedDate() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "winner?date=" + LocalDate.now())
                .with(userHttpBasic(user1)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(ErrorType.WRONG_DATA));
    }

    @Test
    void getWinning() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "winning")
                .with(userHttpBasic(user1)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(restaurant2));
    }

    @Test
    void getAllWithActualMenu() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(user1)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(List.of(restaurant3, restaurant2)));
    }

    @Test
    void getVotesCount() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + FIRST_RESTAURANT_ID + "/rating?date=" + MenuTestData.DATE_12_01)
                .with(userHttpBasic(user1)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(FIRST_RESTAURANT_COUNT_BY_2020_12_01));
    }

    @Test
    void getVotesCountByNullDate() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + (FIRST_RESTAURANT_ID + 1) + "/rating")
                .with(userHttpBasic(user1)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(SECOND_RESTAURANTS_ACTUAL_COUNTS));
    }

    @Test
    void getVotesCountByNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + NOT_FOUND_ID + "/rating")
                .with(userHttpBasic(user1)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(errorType(ErrorType.NOT_FOUND));
    }

    @Test
    void getVotesCountByIllegalDate() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + FIRST_RESTAURANT_ID + "/rating?date=1")
                .with(userHttpBasic(user1)))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(errorType(ErrorType.APP_ERROR));
    }

    @Test
    void vote() throws Exception {
        VoteUtil.prepareEndVoteTimeForPassTests();
        perform(MockMvcRequestBuilders.post(REST_URL + (FIRST_RESTAURANT_ID + 1) + "/vote")
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNoContent());

        VOTE_MATCHER.assertMatch(service.getVotesCount(FIRST_RESTAURANT_ID + 1, null), SECOND_RESTAURANTS_ACTUAL_COUNTS + 1);
    }

    @Test
    void voteForNotFound() throws Exception {
        VoteUtil.prepareEndVoteTimeForPassTests();
        perform(MockMvcRequestBuilders.post(REST_URL + NOT_FOUND_ID + "/vote")
                .with(userHttpBasic(user1)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(ErrorType.WRONG_DATA));
    }

    @Test
    void voteAfterDeadline() throws Exception {
        VoteUtil.prepareEndVoteTimeForFailTests();
        perform(MockMvcRequestBuilders.post(REST_URL + (FIRST_RESTAURANT_ID + 1) + "/vote")
                .with(userHttpBasic(user1)))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(errorType(ErrorType.FORBIDDEN_OPERATION));
    }
}