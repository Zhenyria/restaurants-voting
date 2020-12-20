package repository;

import model.Dish;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

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

    public boolean delete(int id) {
        return repository.delete(id) != 0;
    }

    public Dish get(int id) {
        return repository.findById(id).orElse(null);
    }

    public List<Dish> getAll() {
        return repository.findAll(SORT_NAME_PRICE);
    }
}
