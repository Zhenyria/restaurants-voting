package ru.zhenyria.restaurants.service;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.zhenyria.restaurants.model.Dish;
import ru.zhenyria.restaurants.repository.DishRepository;

import java.util.List;

import static ru.zhenyria.restaurants.util.ValidationUtil.checkExisting;
import static ru.zhenyria.restaurants.util.ValidationUtil.checkUsing;

@Service
public class DishService {
    private static final String NULL_DISH_MSG = "Dish must be not null";
    private static final Sort SORT_NAME_PRICE = Sort.by(Sort.Direction.ASC, "name", "price");

    private final DishRepository repository;

    public DishService(DishRepository repository) {
        this.repository = repository;
    }

    public Dish create(Dish dish) {
        Assert.notNull(dish, NULL_DISH_MSG);
        return checkExisting(repository.save(dish));
    }

    public Dish get(int id) {
        return checkExisting(repository.getById(id));
    }

    public List<Dish> getAll() {
        return repository.findAll(SORT_NAME_PRICE);
    }

    public void update(Dish dish) {
        Assert.notNull(dish, NULL_DISH_MSG);
        checkExisting(repository.save(dish));
    }

    @Transactional
    public void delete(int id) {
        checkUsing(repository.isUsed(id).isPresent());
        checkExisting(repository.delete(id) != 0);
    }
}
