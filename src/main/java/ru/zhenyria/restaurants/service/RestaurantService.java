package ru.zhenyria.restaurants.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.zhenyria.restaurants.model.Restaurant;
import ru.zhenyria.restaurants.repository.RestaurantRepository;

import java.time.LocalDate;
import java.util.List;

import static ru.zhenyria.restaurants.util.ValidationUtil.checkDate;
import static ru.zhenyria.restaurants.util.ValidationUtil.checkExisting;
import static ru.zhenyria.restaurants.util.VoteUtil.isCanReVote;

@Service
public class RestaurantService {
    private static final String NULL_RESTAURANT_MSG = "Restaurant must be not null";

    private final RestaurantRepository repository;

    public RestaurantService(RestaurantRepository repository) {
        this.repository = repository;
    }

    public Restaurant create(Restaurant restaurant) {
        Assert.notNull(restaurant, NULL_RESTAURANT_MSG);
        return checkExisting(repository.save(restaurant));
    }

    public Restaurant get(int id) {
        return checkExisting(repository.get(id));
    }

    public void update(Restaurant restaurant) {
        Assert.notNull(restaurant, NULL_RESTAURANT_MSG);
        checkExisting(repository.save(restaurant));
    }

    public void delete(int id) {
        checkExisting(repository.delete(id));
    }

    public List<Restaurant> getAllWithActualMenu() {
        return repository.getAllWithActualMenu(LocalDate.now());
    }

    public List<Restaurant> getAllWithoutActualMenu() {
        return repository.getAllWithoutActualMenu(LocalDate.now());
    }

    public List<Restaurant> getAll() {
        return repository.getAll();
    }

    public int getVotesCount(int id, LocalDate date) {
        checkExisting(repository.isExist(id));
        return repository.getVotesCountByDate(id, date == null ? LocalDate.now() : date);
    }

    public Restaurant getWinning() {
        return checkExisting(repository.getWinnerByDate(LocalDate.now()));
    }

    public Restaurant getWinner(LocalDate date) {
        checkDate(date);
        return checkExisting(repository.getWinnerByDate(date == null ? LocalDate.now().minusDays(1) : date));
    }

    public Restaurant getWinnerByDate(LocalDate date) {
        return checkExisting(repository.getWinnerByDate(date));
    }

    @Transactional
    public void vote(int id, int userId) {
        if (!repository.isVoting(userId)) {
            repository.vote(id, userId);
        } else {
            if (isCanReVote()) {
                repository.reVote(id, userId);
            } else {
                throw new UnsupportedOperationException("Re-voting after 11:00 is impossible");
            }
        }
    }
}