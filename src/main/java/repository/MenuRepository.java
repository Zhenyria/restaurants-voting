package repository;

import model.Menu;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class MenuRepository {
    private static final Sort SORT_DATE = Sort.by(Sort.Direction.DESC, "date");

    private final CrudMenuRepository repository;

    public MenuRepository(CrudMenuRepository repository) {
        this.repository = repository;
    }

    public Menu save(Menu menu) {
        return repository.save(menu);
    }

    public boolean delete(int id) {
        return repository.delete(id) != 0;
    }

    public Menu get(int id) {
        return repository.findById(id).orElse(null);
    }

    public List<Menu> getAll() {
        return repository.findAll(SORT_DATE);
    }

    public List<Menu> getAllByDate(LocalDate date) {
        return repository.getAllByDate(date);
    }

    public List<Menu> getAllForRestaurant(int id) {
        return repository.getAllForRestaurant(id);
    }

    public Menu getForRestaurantByDate(int id, LocalDate date) {
        return repository.getForRestaurantByDate(id, date);
    }
}
