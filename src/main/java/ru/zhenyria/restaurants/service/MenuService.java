package ru.zhenyria.restaurants.service;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.zhenyria.restaurants.model.Menu;
import ru.zhenyria.restaurants.repository.MenuRepository;

import java.time.LocalDate;
import java.util.List;

import static ru.zhenyria.restaurants.util.ValidationUtil.checkExisting;

@Service
public class MenuService {
    private static final String NULL_MENU_MSG = "Menu must be not null";

    private final MenuRepository repository;

    public MenuService(MenuRepository repository) {
        this.repository = repository;
    }

    public Menu create(Menu menu) {
        Assert.notNull(menu, NULL_MENU_MSG);
        return checkExisting(repository.save(menu));
    }

    public Menu get(int id) {
        return checkExisting(repository.get(id));
    }

    public Menu getActual(int id) {
        return getForRestaurantByDate(id, LocalDate.now());
    }

    public Menu getForRestaurantByDate(int id, LocalDate date) {
        return checkExisting(repository.getForRestaurantByDate(id, date));
    }

    public List<Menu> getAllActual() {
        return getAllByDate(LocalDate.now());
    }

    public List<Menu> getAllByDate(LocalDate date) {
        return repository.getAllByDate(date);
    }

    public List<Menu> getAll() {
        return repository.getAll();
    }

    public List<Menu> getAllForRestaurant(int id) {
        return repository.getAllForRestaurant(id);
    }

    public void update(Menu menu) {
        Assert.notNull(menu, NULL_MENU_MSG);
        checkExisting(repository.save(menu));
    }

    public void delete(int id) {
        checkExisting(repository.delete(id));
    }
}
