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
        log.info("create menu {}", menu);
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

    public Menu getForRestaurantByDate(int id, LocalDate date) {
        log.info("get menu for restaurant {} by date {}", id, date);
        return service.getForRestaurantByDate(id, date);
    }

    public List<Menu> getAllActual() {
        log.info("get all actual menus");
        return service.getAllActual();
    }

    public List<Menu> getAllByDate(LocalDate date) {
        log.info("get all menus by date {}", date);
        return service.getAllByDate(date);
    }

    public List<Menu> getAllForRestaurant(int id) {
        log.info("get all menus for restaurant {}", id);
        return service.getAllForRestaurant(id);
    }

    public List<Menu> getAll() {
        log.info("get all menus");
        return service.getAll();
    }

    public void update(MenuTo menu) {
        log.info("update menu {}", menu.id());
        service.update(menu);
    }

    public void delete(int id) {
        log.info("delete menu {}", id);
        service.delete(id);
    }
}
