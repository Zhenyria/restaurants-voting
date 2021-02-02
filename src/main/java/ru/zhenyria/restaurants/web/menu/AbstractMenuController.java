package ru.zhenyria.restaurants.web.menu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.zhenyria.restaurants.model.Menu;
import ru.zhenyria.restaurants.service.MenuService;
import ru.zhenyria.restaurants.to.MenuTo;

import java.time.LocalDate;
import java.util.List;

public abstract class AbstractMenuController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    protected MenuService service;

    public Menu create(MenuTo menu) {
        log.info("create menu {} for restaurant {}", menu, menu.getRestaurantId());
        return service.create(menu);
    }

    public Menu get(int id) {
        log.info("get menu {}", id);
        return service.get(id);
    }

    public Menu getActual(int id) {
        log.info("get actual menu for restaurant {}", id);
        return service.getActual(id);
    }

    public List<Menu> getAllActual() {
        log.info("get all actual menus");
        return service.getAllActual();
    }

    public List<Menu> getAll(LocalDate date, Integer restaurantId) {
        log.info("get all menus by date {} for restaurant {}", date, restaurantId);
        return service.getAll(date, restaurantId);
    }

    public void update(MenuTo menu) {
        log.info("update menu {}", menu.id());
        service.update(menu);
    }

    public void addDish(int id, int dishId) {
        log.info("add dish {} to menu {}", dishId, id);
        service.addDish(id, dishId);
    }

    public void deleteDish(int id, int dishId) {
        log.info("delete dish {} from menu {}", dishId, id);
        service.deleteDish(id, dishId);
    }

    public void delete(int id) {
        log.info("delete menu {}", id);
        service.delete(id);
    }
}
