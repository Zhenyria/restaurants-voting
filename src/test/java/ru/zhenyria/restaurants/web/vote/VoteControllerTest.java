package ru.zhenyria.restaurants.web.vote;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.zhenyria.restaurants.MenuTestData;
import ru.zhenyria.restaurants.service.VoteService;
import ru.zhenyria.restaurants.util.VoteUtil;
import ru.zhenyria.restaurants.util.exception.ErrorType;
import ru.zhenyria.restaurants.web.AbstractControllerTest;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.zhenyria.restaurants.RestaurantTestData.*;
import static ru.zhenyria.restaurants.TestUtil.userHttpBasic;
import static ru.zhenyria.restaurants.UserTestData.*;

public class VoteControllerTest extends AbstractControllerTest {
    private static final String URL_PREFIX = "/rest/restaurants/";
    private static final String URL_POSTFIX = "/votes";

    @Autowired
    private VoteService service;

    @Test
    void getVotesCount() throws Exception {
        perform(MockMvcRequestBuilders.get(
                URL_PREFIX + FIRST_RESTAURANT_ID + URL_POSTFIX + "?date=" + MenuTestData.DATE_12_01)
                .with(userHttpBasic(user1)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(FIRST_RESTAURANT_COUNT_BY_2020_12_01));
    }

    @Test
    void getVotesCountByNullDate() throws Exception {
        perform(MockMvcRequestBuilders.get(URL_PREFIX + (FIRST_RESTAURANT_ID + 1) + URL_POSTFIX)
                .with(userHttpBasic(user1)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(SECOND_RESTAURANTS_ACTUAL_COUNTS));
    }

    @Test
    void getVotesCountByNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(URL_PREFIX + NOT_FOUND_ID + URL_POSTFIX)
                .with(userHttpBasic(user1)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(errorType(ErrorType.NOT_FOUND));
    }

    @Test
    void getVotesCountByIllegalDate() throws Exception {
        perform(MockMvcRequestBuilders.get(URL_PREFIX + FIRST_RESTAURANT_ID + URL_POSTFIX + "?date=1")
                .with(userHttpBasic(user1)))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(errorType(ErrorType.APP_ERROR));
    }

    @Test
    void vote() throws Exception {
        VoteUtil.prepareEndVoteTimeForPassTests();
        perform(MockMvcRequestBuilders.post(URL_PREFIX + (FIRST_RESTAURANT_ID + 1) + URL_POSTFIX)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNoContent());

        VOTE_MATCHER.assertMatch(
                service.getVotesCount(FIRST_RESTAURANT_ID + 1, null),
                SECOND_RESTAURANTS_ACTUAL_COUNTS + 1);
    }

    @Test
    void voteForNotFound() throws Exception {
        VoteUtil.prepareEndVoteTimeForPassTests();
        perform(MockMvcRequestBuilders.post(URL_PREFIX + NOT_FOUND_ID + URL_POSTFIX)
                .with(userHttpBasic(user1)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(ErrorType.WRONG_DATA));
    }

    @Test
    void voteAfterDeadline() throws Exception {
        VoteUtil.prepareEndVoteTimeForFailTests();
        perform(MockMvcRequestBuilders.post(URL_PREFIX + (FIRST_RESTAURANT_ID + 1) + URL_POSTFIX)
                .with(userHttpBasic(user1)))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(errorType(ErrorType.VOTING_ERROR));
    }
}
