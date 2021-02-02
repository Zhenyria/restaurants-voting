package ru.zhenyria.restaurants.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.zhenyria.restaurants.model.Dish;
import ru.zhenyria.restaurants.model.Menu;
import ru.zhenyria.restaurants.repository.MenuRepository;
import ru.zhenyria.restaurants.to.MenuTo;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static ru.zhenyria.restaurants.util.ValidationUtil.checkExisting;

@Service
public class MenuService {
    private static final String NULL_MENU_MSG = "Menu must be not null";
    private static final Sort SORT_DATE =
            Sort.by(Sort.Direction.DESC, "date")
                    .and(Sort.by(Sort.Direction.ASC, "restaurant.name"));

    private final MenuRepository repository;
    private final RestaurantService restaurantService;
    private final DishService dishService;

    public MenuService(MenuRepository repository, RestaurantService restaurantService, DishService dishService) {
        this.repository = repository;
        this.restaurantService = restaurantService;
        this.dishService = dishService;
    }

    @Caching(evict = {
            @CacheEvict(value = "menusList", allEntries = true),
            @CacheEvict(value = "menus", allEntries = true)
    })
    @Transactional
    public Menu create(MenuTo menu) {
        Assert.notNull(menu, NULL_MENU_MSG);
        return repository.save(
                new Menu(menu.getId(),
                        restaurantService.get(menu.getRestaurantId()),
                        menu.getDishIds().stream().map(dishService::get).collect(Collectors.toSet())));
    }

    public Menu get(int id) {
        return checkExisting(repository.get(id));
    }

    @Cacheable("menus")
    public Menu getActual(int id) {
        return repository.getByRestaurantAndDate(id, LocalDate.now());
    }

    @Cacheable("menusList")
    public List<Menu> getAllActual() {
        return repository.getAllByDate(LocalDate.now());
    }

    public List<Menu> getAll(LocalDate date, Integer restaurantId) {
        final boolean isRestaurantIdNull = (restaurantId == null);
        final boolean isDateNull = (date == null);

        if (isDateNull && isRestaurantIdNull) {
            return repository.findAll(SORT_DATE);
        }
        if (isRestaurantIdNull) {
            return repository.getAllByDate(date);
        }
        if (isDateNull) {
            return repository.getAllByRestaurant(restaurantId);
        }
        return List.of(checkExisting(repository.getByRestaurantAndDate(restaurantId, date)));
    }

    @Caching(evict = {
            @CacheEvict(value = "menusList", allEntries = true),
            @CacheEvict(value = "menus", allEntries = true)
    })
    @Transactional
    public void update(MenuTo updated) {
        Assert.notNull(updated, NULL_MENU_MSG);
        Menu menu = get(updated.id());
        menu.setDishes(updated.getDishIds().stream().map(dishService::getReference).collect(Collectors.toSet()));
        repository.save(menu);
    }

    @Caching(evict = {
            @CacheEvict(value = "menusList", allEntries = true),
            @CacheEvict(value = "menus", allEntries = true)
    })
    @Transactional
    public void addDish(int id, int dishId) {
        Dish dish = dishService.get(dishId);
        Menu menu = get(id);
        menu.addDish(dish);
        checkExisting(repository.save(menu));
    }

    @Caching(evict = {
            @CacheEvict(value = "menusList", allEntries = true),
            @CacheEvict(value = "menus", allEntries = true)
    })
    @Transactional
    public void deleteDish(int id, int dishId) {
        Dish dish = dishService.get(dishId);
        Menu menu = get(id);
        menu.deleteDish(dish);
        checkExisting(repository.save(menu));
    }

    @Caching(evict = {
            @CacheEvict(value = "menusList", allEntries = true),
            @CacheEvict(value = "menus", allEntries = true)
    })
    public void delete(int id) {
        checkExisting(repository.delete(id) != 0);
    }
}
