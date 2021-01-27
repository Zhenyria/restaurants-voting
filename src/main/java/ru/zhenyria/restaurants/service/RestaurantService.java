package ru.zhenyria.restaurants.service;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.zhenyria.restaurants.model.Restaurant;
import ru.zhenyria.restaurants.repository.RestaurantRepository;

import java.time.LocalDate;
import java.util.List;

import static ru.zhenyria.restaurants.util.ValidationUtil.checkDate;
import static ru.zhenyria.restaurants.util.ValidationUtil.checkExisting;

@Service
public class RestaurantService {
    private static final String NULL_RESTAURANT_MSG = "Restaurant must be not null";
    private static final Sort SORT_NAME = Sort.by(Sort.Direction.ASC, "name");

    private final RestaurantRepository repository;

    public RestaurantService(RestaurantRepository repository) {
        this.repository = repository;
    }

    public Restaurant create(Restaurant restaurant) {
        Assert.notNull(restaurant, NULL_RESTAURANT_MSG);
        return checkExisting(repository.save(restaurant));
    }

    public Restaurant get(int id) {
        return checkExisting(repository.findById(id).orElse(null));
    }

    public Restaurant getReference(int id) {
        return repository.getOne(id);
    }

    public void update(Restaurant restaurant) {
        Assert.notNull(restaurant, NULL_RESTAURANT_MSG);
        checkExisting(repository.save(restaurant));
    }

    public void delete(int id) {
        checkExisting(repository.delete(id) != 0);
    }

    public List<Restaurant> getAllWithActualMenu() {
        return repository.getAllWithActualMenu(LocalDate.now());
    }

    public List<Restaurant> getAllWithoutActualMenu() {
        return repository.getAllWithoutActualMenu(LocalDate.now());
    }

    public List<Restaurant> getAll() {
        return repository.findAll(SORT_NAME);
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
}