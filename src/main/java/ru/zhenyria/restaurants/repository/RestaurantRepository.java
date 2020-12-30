package ru.zhenyria.restaurants.repository;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import ru.zhenyria.restaurants.model.Restaurant;

import java.time.LocalDate;
import java.util.List;

@Repository
public class RestaurantRepository {
    private static final Sort SORT_NAME = Sort.by(Sort.Direction.ASC, "name");

    private final CrudRestaurantRepository repository;

    public RestaurantRepository(CrudRestaurantRepository repository) {
        this.repository = repository;
    }

    public Restaurant save(Restaurant restaurant) {
        return repository.save(restaurant);
    }

    public boolean delete(int id) {
        return repository.delete(id) != 0;
    }

    public Restaurant get(int id) {
        return repository.findById(id).orElse(null);
    }

    public Restaurant getReference(int id) {
        return repository.getOne(id);
    }

    public boolean isExist(int id) {
        return repository.existsById(id);
    }

    public List<Restaurant> getAllWithActualMenu(LocalDate date) {
        return repository.getAllWithActualMenu(date);
    }

    public List<Restaurant> getAllWithoutActualMenu(LocalDate date) {
        return repository.getAllWithoutActualMenu(date);
    }

    public List<Restaurant> getAll() {
        return repository.findAll(SORT_NAME);
    }

    public Restaurant getWinnerByDate(LocalDate date) {
        return repository.getWinnerByDate(date);
    }

    public int countVotesByDate(int id, LocalDate date) {
        return repository.countVotesByDate(id, date);
    }

    public boolean isVoting(int id) {
        return repository.countUserVotesToday(id) > 0;
    }

    public void vote(int id, int userId) {
        repository.vote(id, userId);
    }

    public void reVote(int id, int userId) {
        repository.reVote(id, userId);
    }
}
