package ru.zhenyria.restaurants.repository;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import ru.zhenyria.restaurants.model.Menu;

import java.time.LocalDate;
import java.util.List;

@Repository
public class MenuRepository {
    private static final Sort SORT_DATE =
            Sort.by(Sort.Direction.DESC, "date")
                    .and(Sort.by(Sort.Direction.ASC, "restaurant.name"));

    private final CrudMenuRepository repository;

    public MenuRepository(CrudMenuRepository repository) {
        this.repository = repository;
    }

    public Menu save(Menu menu) {
        return repository.save(menu);
    }

    public Menu get(int id) {
        return repository.findById(id).orElse(null);
    }

    public Menu getReference(int id) {
        return repository.getOne(id);
    }

    public List<Menu> getAll() {
        return repository.findAll(SORT_DATE);
    }

    public Menu getByRestaurantAndDate(int id, LocalDate date) {
        return repository.getByRestaurantAndDate(id, date);
    }

    public List<Menu> getAllByDate(LocalDate date) {
        return repository.getAllByDate(date);
    }

    public List<Menu> getAllByRestaurant(int id) {
        return repository.getAllByRestaurant(id);
    }

    public boolean delete(int id) {
        return repository.delete(id) != 0;
    }
}
