package ru.zhenyria.restaurants.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.zhenyria.restaurants.model.Menu;
import ru.zhenyria.restaurants.repository.DishRepository;
import ru.zhenyria.restaurants.repository.MenuRepository;
import ru.zhenyria.restaurants.repository.RestaurantRepository;
import ru.zhenyria.restaurants.to.MenuTo;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static ru.zhenyria.restaurants.util.ValidationUtil.checkExisting;

@Service
public class MenuService {
    private static final String NULL_MENU_MSG = "MenuTo must be not null";

    private final MenuRepository repository;
    private final RestaurantRepository restaurantRepository;
    private final DishRepository dishRepository;

    public MenuService(MenuRepository repository, RestaurantRepository restaurantRepository, DishRepository dishRepository) {
        this.repository = repository;
        this.restaurantRepository = restaurantRepository;
        this.dishRepository = dishRepository;
    }

    @Transactional
    public Menu create(MenuTo menu) {
        Assert.notNull(menu, NULL_MENU_MSG);
        return checkExisting(repository.save(getFromTo(menu)));
    }

    public Menu get(int id) {
        return repository.get(id);
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

    @Transactional
    public void update(MenuTo menu) {
        Assert.notNull(menu, NULL_MENU_MSG);
        checkExisting(repository.save(getFromTo(menu)));
    }

    public void delete(int id) {
        checkExisting(repository.delete(id));
    }

    /**
     * Util method, works with restaurant and dish repository to get references objects for creating the menu
     * and return the menu
     *
     * @param menu DTO menu
     */
    private Menu getFromTo(MenuTo menu) {
        return new Menu(
                restaurantRepository.getReference(menu.getRestaurantId()),
                menu.getDate(),
                Arrays.stream(menu.getDishIds())
                        .map(dishRepository::getReference)
                        .collect(Collectors.toList()));
    }
}
