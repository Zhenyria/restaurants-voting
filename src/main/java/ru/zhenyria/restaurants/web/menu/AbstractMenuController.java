package ru.zhenyria.restaurants.web.menu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.zhenyria.restaurants.model.Menu;
import ru.zhenyria.restaurants.service.MenuService;

import java.time.LocalDate;
import java.util.List;

public abstract class AbstractMenuController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    protected MenuService service;

    public Menu create(Menu menu) {
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

    public List<Menu> getByRestaurant(int id, LocalDate date) {
        log.info("get menu for restaurant {} by date {}", id, date);
        return service.getByRestaurant(id, date);
    }

    public List<Menu> getAllActual() {
        log.info("get all actual menus");
        return service.getAllActual();
    }

    public List<Menu> getAll(LocalDate date) {
        log.info("get all menus by date {}", date);
        return service.getAll(date);
    }

    public void update(Menu menu) {
        log.info("update menu {}", menu.id());
        service.update(menu);
    }

    public void delete(int id) {
        log.info("delete menu {}", id);
        service.delete(id);
    }
}
