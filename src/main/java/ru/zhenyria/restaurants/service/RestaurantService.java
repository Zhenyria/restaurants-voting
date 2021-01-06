package ru.zhenyria.restaurants.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.zhenyria.restaurants.model.Restaurant;
import ru.zhenyria.restaurants.repository.RestaurantRepository;

import java.time.LocalDate;
import java.util.List;

import static ru.zhenyria.restaurants.util.Util.isCanReVote;
import static ru.zhenyria.restaurants.util.ValidationUtil.checkExisting;

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

    public int getCount(int id) {
        checkExisting(repository.isExist(id));
        return repository.countVotesByDate(id, LocalDate.now());
    }

    public int getCountByDate(int id, LocalDate date) {
        checkExisting(repository.isExist(id));
        return repository.countVotesByDate(id, date);
    }

    public Restaurant getWinning() {
        return checkExisting(repository.getWinnerByDate(LocalDate.now()));
    }

    //todo: test for winner??
    public Restaurant getWinner() {
        return checkExisting(repository.getWinnerByDate(LocalDate.now().minusDays(1)));
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
