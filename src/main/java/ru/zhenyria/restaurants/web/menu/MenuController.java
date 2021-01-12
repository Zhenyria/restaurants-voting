package ru.zhenyria.restaurants.web.menu;

import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import ru.zhenyria.restaurants.model.Menu;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = MenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class MenuController extends AbstractMenuController {
    static final String REST_URL = "/rest/profile/restaurants";
    static final String MENU_URL = "/menus";

    @Override
    @GetMapping(MENU_URL + "/{id}")
    public Menu get(@PathVariable int id) {
        return super.get(id);
    }

    @Override
    @GetMapping("/{id}" + MENU_URL + "/actual")
    public Menu getActual(@PathVariable int id) {
        return super.getActual(id);
    }

    @Override
    @GetMapping("/{id}" + MENU_URL)
    public List<Menu> getByRestaurant(@PathVariable int id, @Nullable @RequestParam(value = "date") LocalDate date) {
        return super.getByRestaurant(id, date);
    }

    @Override
    @GetMapping(MENU_URL + "/actual")
    public List<Menu> getAllActual() {
        return super.getAllActual();
    }

    @Override
    @GetMapping(MENU_URL)
    public List<Menu> getAll(@Nullable @RequestParam(value = "date") LocalDate date) {
        return super.getAll(date);
    }
}