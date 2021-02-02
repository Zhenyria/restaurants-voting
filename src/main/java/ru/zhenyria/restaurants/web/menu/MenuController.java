package ru.zhenyria.restaurants.web.menu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import ru.zhenyria.restaurants.model.Menu;
import ru.zhenyria.restaurants.service.MenuService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = MenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class MenuController {
    static final String REST_URL = "/rest";
    static final String MENUS_URL = "/menus";
    private final Logger log = LoggerFactory.getLogger(getClass());

    protected final MenuService service;

    public MenuController(MenuService service) {
        this.service = service;
    }

    @GetMapping(MENUS_URL + "/{id}")
    public Menu get(@PathVariable int id) {
        log.info("get menu {}", id);
        return service.get(id);
    }

    @GetMapping("/restaurants/{id}" + MENUS_URL + "/actual")
    public Menu getActual(@PathVariable int id) {
        log.info("get actual menu for restaurant {}", id);
        return service.getActual(id);
    }

    @GetMapping(MENUS_URL + "/actual")
    public List<Menu> getAllActual() {
        log.info("get all actual menus");
        return service.getAllActual();
    }

    @GetMapping(MENUS_URL)
    public List<Menu> getAll(@Nullable @RequestParam(value = "date") LocalDate date,
                             @Nullable @RequestParam(value = "restaurantId") Integer restaurantId) {
        log.info("get all menus by date {} for restaurant {}", date, restaurantId);
        return service.getAll(date, restaurantId);
    }
}