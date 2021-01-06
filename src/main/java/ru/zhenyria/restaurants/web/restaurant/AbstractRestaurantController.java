package ru.zhenyria.restaurants.web.restaurant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.zhenyria.restaurants.model.Restaurant;
import ru.zhenyria.restaurants.service.RestaurantService;

import java.time.LocalDate;
import java.util.List;

import static ru.zhenyria.restaurants.util.ValidationUtil.checkNew;

public abstract class AbstractRestaurantController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    protected RestaurantService service;

    public Restaurant create(Restaurant restaurant) {
        log.info("create restaurant {}", restaurant);
        checkNew(restaurant);
        return service.create(restaurant);
    }

    public Restaurant get(int id) {
        log.info("get restaurant {}", id);
        return service.get(id);
    }

    public List<Restaurant> getAll() {
        log.info("get all restaurants");
        return service.getAll();
    }

    public List<Restaurant> getAllWithActualMenu() {
        log.info("get all restaurants with actual menu");
        return service.getAllWithActualMenu();
    }

    public List<Restaurant> getAllWithoutActualMenu() {
        log.info("get all restaurants without actual menu");
        return service.getAllWithoutActualMenu();
    }

    public Restaurant getWinner() {
        log.info("get restaurant-winner");
        return service.getWinner();
    }

    public Restaurant getWinnerByDate(LocalDate date) {
        log.info("get restaurant-winner by date {}", date);
        return service.getWinnerByDate(date);
    }

    public Restaurant getWinning() {
        log.info("get winning restaurant");
        return service.getWinning();
    }

    public int countVotes(int id) {
        log.info("count votes for restaurant {}", id);
        return service.countVotes(id);
    }

    public int countVotesByDate(int id, LocalDate date) {
        log.info("count votes for restaurant {} by date {}", id, date);
        return service.countVotesByDate(id, date);
    }

    public void vote(int id, int userId) {
        log.info("vote by restaurant {}", id);
        service.vote(id, userId);
    }

    public void update(Restaurant restaurant) {
        log.info("update restaurant {}", restaurant.id());
        service.update(restaurant);
    }

    public void delete(int id) {
        log.info("delete restaurant {}", id);
        service.delete(id);
    }
}
