package ru.zhenyria.restaurants.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
        // it's must be because work with date is wrong
        menu.setDate(LocalDate.now());
        return checkExisting(repository.save(menu));
    }

    public Menu get(int id) {
        return checkExisting(repository.get(id));
    }

    public Menu getActual(int id) {
        return repository.getByRestaurantAndDate(id, LocalDate.now());
    }

    public List<Menu> getByRestaurant(int id, LocalDate date) {
        if (date == null) {
            return repository.getAllByRestaurant(id);
        }
        return List.of(checkExisting(repository.getByRestaurantAndDate(id, date)));
    }

    public List<Menu> getAllActual() {
        return repository.getAllByDate(LocalDate.now());
    }

    public List<Menu> getAll(LocalDate date) {
        if (date == null) {
            return repository.getAll();
        }
        return repository.getAllByDate(date);
    }

    @Transactional
    public void update(Menu menu) {
        Assert.notNull(menu, NULL_MENU_MSG);
        // it's must be because work with date is wrong
        checkExisting(repository.save(updateFrom(menu, repository.get(menu.id()))));
    }

    public void delete(int id) {
        checkExisting(repository.delete(id));
    }

    private Menu updateFrom(Menu updated, Menu actual) {
        actual.setDishes(updated.getDishes());
        return actual;
    }
}
