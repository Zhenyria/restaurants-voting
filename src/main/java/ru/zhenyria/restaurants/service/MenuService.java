package ru.zhenyria.restaurants.service;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.zhenyria.restaurants.model.Menu;
import ru.zhenyria.restaurants.repository.MenuRepository;
import ru.zhenyria.restaurants.to.MenuTo;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static ru.zhenyria.restaurants.util.ValidationUtil.checkExisting;

@Service
public class MenuService {
    private static final String NULL_MENU_MSG = "Menu must be not null";
    private static final Sort SORT_DATE =
            Sort.by(Sort.Direction.DESC, "date")
                    .and(Sort.by(Sort.Direction.ASC, "restaurant.name"));

    private final MenuRepository repository;

    private final RestaurantService restaurantService;

    public MenuService(MenuRepository repository, RestaurantService restaurantService) {
        this.repository = repository;
        this.restaurantService = restaurantService;
    }

    @Transactional
    public Menu create(MenuTo menu) {
        Assert.notNull(menu, NULL_MENU_MSG);
        return checkExisting(saveFromTo(menu));
    }

    public Menu get(int id) {
        return checkExisting(repository.findById(id).orElse(null));
    }

    public Menu getActual(int id) {
        return repository.getByRestaurantAndDate(id, LocalDate.now());
    }

    public List<Menu> getAllActual() {
        return repository.getAllByDate(LocalDate.now());
    }

    public List<Menu> getAll(LocalDate date, Integer restaurantId) {
        final boolean isRestaurantIdNull = restaurantId == null;
        final boolean isDateNull = date == null;

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

    @Transactional
    public void update(MenuTo menu) {
        Assert.notNull(menu, NULL_MENU_MSG);
        checkExisting(saveFromTo(menu));
    }

    public void delete(int id) {
        checkExisting(repository.delete(id) != 0);
    }

    private Menu saveFromTo(MenuTo menu) {
        return repository.save(
                new Menu(menu.getId(),
                        menu.isNew() ?
                                restaurantService.get(menu.getRestaurantId()) :
                                restaurantService.getReference(menu.getRestaurantId()),
                        menu.getDishes(),
                        menu.isNew() ?
                                Collections.emptyList() :
                                repository.getOne(menu.getId()).getUsers()));
    }
}
