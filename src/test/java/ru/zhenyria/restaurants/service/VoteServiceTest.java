package ru.zhenyria.restaurants.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import ru.zhenyria.restaurants.MenuTestData;
import ru.zhenyria.restaurants.util.VoteUtil;
import ru.zhenyria.restaurants.util.exception.NotFoundException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.zhenyria.restaurants.RestaurantTestData.*;
import static ru.zhenyria.restaurants.UserTestData.ADMIN_ID;
import static ru.zhenyria.restaurants.UserTestData.NOT_FOUND_ID;

class VoteServiceTest extends AbstractServiceTest {

    @Autowired
    private VoteService service;

    @Test
    void getVotesCount() {
        VOTE_MATCHER.assertMatch(service.getVotesCount(FIRST_RESTAURANT_ID, MenuTestData.DATE_12_01), FIRST_RESTAURANT_COUNT_BY_2020_12_01);
        VOTE_MATCHER.assertMatch(service.getVotesCount(FIRST_RESTAURANT_ID, MenuTestData.DATE_12_02), FIRST_RESTAURANT_COUNT_BY_2020_12_02);
        VOTE_MATCHER.assertMatch(service.getVotesCount(FIRST_RESTAURANT_ID, MenuTestData.DATE_12_03), FIRST_RESTAURANT_COUNT_BY_2020_12_03);
        VOTE_MATCHER.assertMatch(service.getVotesCount(FIRST_RESTAURANT_ID + 1, LocalDate.now()), SECOND_RESTAURANTS_ACTUAL_COUNTS);
    }

    @Test
    void getVotesCountOfNotExist() {
        assertThrows(NotFoundException.class, () -> service.getVotesCount(NOT_FOUND_ID, MenuTestData.DATE_12_02));
    }

    @Test
    void getVotesCountByNullDate() {
        VOTE_MATCHER.assertMatch(service.getVotesCount(FIRST_RESTAURANT_ID, null), FIRST_RESTAURANTS_ACTUAL_COUNTS);
        VOTE_MATCHER.assertMatch(service.getVotesCount(FIRST_RESTAURANT_ID + 1, null), SECOND_RESTAURANTS_ACTUAL_COUNTS);
        VOTE_MATCHER.assertMatch(service.getVotesCount(FIRST_RESTAURANT_ID + 2, null), THIRD_RESTAURANTS_ACTUAL_COUNTS);
    }

    @Test
    void vote() {
        VOTE_MATCHER.assertMatch(service.getVotesCount(FIRST_RESTAURANT_ID + 1, null), SECOND_RESTAURANTS_ACTUAL_COUNTS);
        service.vote(FIRST_RESTAURANT_ID + 1, ADMIN_ID);
        VOTE_MATCHER.assertMatch(service.getVotesCount(FIRST_RESTAURANT_ID + 1, null), SECOND_RESTAURANTS_ACTUAL_COUNTS + 1);
    }

    @Test
    void voteNotExist() {
        assertThrows(DataIntegrityViolationException.class, () -> service.vote(NOT_FOUND_ID, ADMIN_ID));
    }

    @Test
    void voteNotExistUser() {
        assertThrows(DataIntegrityViolationException.class, () -> service.vote(FIRST_RESTAURANT_ID + 1, NOT_FOUND_ID));
    }

    @Test
    void reVote() {
        VoteUtil.prepareEndVoteTimeForPassTests();
        service.vote(FIRST_RESTAURANT_ID + 1, ADMIN_ID);
        VOTE_MATCHER.assertMatch(service.getVotesCount(FIRST_RESTAURANT_ID + 1, null), SECOND_RESTAURANTS_ACTUAL_COUNTS + 1);
        VOTE_MATCHER.assertMatch(service.getVotesCount(FIRST_RESTAURANT_ID + 2, null), THIRD_RESTAURANTS_ACTUAL_COUNTS);
        service.vote(FIRST_RESTAURANT_ID + 2, ADMIN_ID);
        VOTE_MATCHER.assertMatch(service.getVotesCount(FIRST_RESTAURANT_ID + 2, null), THIRD_RESTAURANTS_ACTUAL_COUNTS + 1);
    }

    @Test
    void voteAndreVoteAfterDeadLine() {
        VoteUtil.prepareEndVoteTimeForFailTests();
        service.vote(FIRST_RESTAURANT_ID + 1, ADMIN_ID);
        VOTE_MATCHER.assertMatch(service.getVotesCount(FIRST_RESTAURANT_ID + 1, null), SECOND_RESTAURANTS_ACTUAL_COUNTS + 1);
        assertThrows(UnsupportedOperationException.class, () -> service.vote(FIRST_RESTAURANT_ID + 2, ADMIN_ID));
    }
}
