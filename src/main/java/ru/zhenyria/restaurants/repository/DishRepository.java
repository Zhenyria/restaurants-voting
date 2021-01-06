package ru.zhenyria.restaurants.repository;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import ru.zhenyria.restaurants.model.Dish;

import java.util.List;

@Repository
public class DishRepository {
    private static final Sort SORT_NAME_PRICE = Sort.by(Sort.Direction.ASC, "name", "price");

    private final CrudDishRepository repository;

    public DishRepository(CrudDishRepository repository) {
        this.repository = repository;
    }

    public Dish save(Dish dish) {
        return repository.save(dish);
    }

    public Dish get(int id) {
        return repository.findById(id).orElse(null);
    }

    public Dish getReference(int id) {
        return repository.getOne(id);
    }

    public List<Dish> getAll() {
        return repository.findAll(SORT_NAME_PRICE);
    }

    public boolean isUsing(int id) {
        return repository.countUsing(id) > 0;
    }

    public boolean addToMenu(int menuId, int id) {
        return repository.addToMenu(menuId, id) != 0;
    }

    public boolean deleteFromMenu(int menuId, int id) {
        return repository.deleteFromMenu(menuId, id) != 0;
    }

    public boolean delete(int id) {
        return repository.delete(id) != 0;
    }
}
