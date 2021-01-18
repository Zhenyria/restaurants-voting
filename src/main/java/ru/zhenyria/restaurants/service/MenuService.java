package ru.zhenyria.restaurants.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.zhenyria.restaurants.model.Menu;
import ru.zhenyria.restaurants.repository.MenuRepository;
import ru.zhenyria.restaurants.repository.RestaurantRepository;
import ru.zhenyria.restaurants.to.MenuTo;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static ru.zhenyria.restaurants.util.ValidationUtil.checkExisting;

@Service
public class MenuService {
    private static final String NULL_MENU_MSG = "Menu must be not null";

    private final MenuRepository repository;

    private final RestaurantRepository restaurantRepository;

    public MenuService(MenuRepository repository, RestaurantRepository restaurantRepository) {
        this.repository = repository;
        this.restaurantRepository = restaurantRepository;
    }

    @Transactional
    public Menu create(MenuTo menu) {
        Assert.notNull(menu, NULL_MENU_MSG);
        return checkExisting(saveFromTo(menu));
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
    public void update(MenuTo menu) {
        Assert.notNull(menu, NULL_MENU_MSG);
        checkExisting(saveFromTo(menu));
    }

    public void delete(int id) {
        checkExisting(repository.delete(id));
    }

    private Menu saveFromTo(MenuTo menu) {
        return repository.save(
                new Menu(menu.getId(),
                        menu.isNew() ?
                                restaurantRepository.get(menu.getRestaurantId()) :
                                restaurantRepository.getReference(menu.getRestaurantId()),
                        menu.getDishes(),
                        menu.isNew() ?
                                Collections.emptyList() :
                                repository.getReference(menu.getId()).getUsers()));
    }
}
