package ru.zhenyria.restaurants.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.TransactionSystemException;
import ru.zhenyria.restaurants.MenuTestData;
import ru.zhenyria.restaurants.model.Restaurant;
import ru.zhenyria.restaurants.util.VoteUtil;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.zhenyria.restaurants.RestaurantTestData.*;
import static ru.zhenyria.restaurants.UserTestData.ADMIN_ID;
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
        assertThrows(RuntimeException.class, () -> service.get(NOT_FOUND_ID));
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
    void countVotes() {
        VOTE_MATCHER.assertMatch(service.countVotes(FIRST_RESTAURANT_ID), FIRST_RESTAURANTS_ACTUAL_COUNTS);
        VOTE_MATCHER.assertMatch(service.countVotes(FIRST_RESTAURANT_ID + 1), SECOND_RESTAURANTS_ACTUAL_COUNTS);
        VOTE_MATCHER.assertMatch(service.countVotes(FIRST_RESTAURANT_ID + 2), THIRD_RESTAURANTS_ACTUAL_COUNTS);
    }

    @Test
    void countVotesOfNotExist() {
        assertThrows(RuntimeException.class, () -> service.countVotes(NOT_FOUND_ID));
    }

    @Test
    void countVotesByDate() {
        VOTE_MATCHER.assertMatch(service.countVotesByDate(FIRST_RESTAURANT_ID, MenuTestData.DATE_12_01), FIRST_RESTAURANT_COUNT_BY_2020_12_01);
        VOTE_MATCHER.assertMatch(service.countVotesByDate(FIRST_RESTAURANT_ID, MenuTestData.DATE_12_02), FIRST_RESTAURANT_COUNT_BY_2020_12_02);
        VOTE_MATCHER.assertMatch(service.countVotesByDate(FIRST_RESTAURANT_ID, MenuTestData.DATE_12_03), FIRST_RESTAURANT_COUNT_BY_2020_12_03);
        VOTE_MATCHER.assertMatch(service.countVotesByDate(FIRST_RESTAURANT_ID + 1, LocalDate.now()), SECOND_RESTAURANTS_ACTUAL_COUNTS);
    }

    @Test
    void countVotesByDateForNotExist() {
        assertThrows(RuntimeException.class, () -> service.countVotesByDate(NOT_FOUND_ID, MenuTestData.DATE_12_02));
    }

    @Test
    void getWinning() {
        RESTAURANT_MATCHER.assertMatch(service.getWinning(), restaurant2);
    }

    @Test
    void getWinnerByDate() {
        RESTAURANT_MATCHER.assertMatch(service.getWinnerByDate(MenuTestData.DATE_12_01), restaurant1);
    }

    @Test
    void vote() {
        VOTE_MATCHER.assertMatch(service.countVotes(FIRST_RESTAURANT_ID + 1), SECOND_RESTAURANTS_ACTUAL_COUNTS);
        service.vote(FIRST_RESTAURANT_ID + 1, ADMIN_ID);
        VOTE_MATCHER.assertMatch(service.countVotes(FIRST_RESTAURANT_ID + 1), SECOND_RESTAURANTS_ACTUAL_COUNTS + 1);
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
        VOTE_MATCHER.assertMatch(service.countVotes(FIRST_RESTAURANT_ID + 1), SECOND_RESTAURANTS_ACTUAL_COUNTS + 1);
        VOTE_MATCHER.assertMatch(service.countVotes(FIRST_RESTAURANT_ID + 2), THIRD_RESTAURANTS_ACTUAL_COUNTS);
        service.vote(FIRST_RESTAURANT_ID + 2, ADMIN_ID);
        VOTE_MATCHER.assertMatch(service.countVotes(FIRST_RESTAURANT_ID + 2), THIRD_RESTAURANTS_ACTUAL_COUNTS + 1);
    }

    @Test
    void voteAndreVoteAfterElevenOClock() {
        VoteUtil.prepareEndVoteTimeForFailTests();
        service.vote(FIRST_RESTAURANT_ID + 1, ADMIN_ID);
        VOTE_MATCHER.assertMatch(service.countVotes(FIRST_RESTAURANT_ID + 1), SECOND_RESTAURANTS_ACTUAL_COUNTS + 1);
        assertThrows(RuntimeException.class, () -> service.vote(FIRST_RESTAURANT_ID + 2, ADMIN_ID));
    }
}
