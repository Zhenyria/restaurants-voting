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
    static final String REST_URL = "/rest";
    static final String MENUS_URL = "/menus";

    @Override
    @GetMapping(MENUS_URL + "/{id}")
    public Menu get(@PathVariable int id) {
        return super.get(id);
    }

    @Override
    @GetMapping("/restaurants/{id}" + MENUS_URL + "/actual")
    public Menu getActual(@PathVariable int id) {
        return super.getActual(id);
    }

    @Override
    @GetMapping(MENUS_URL + "/actual")
    public List<Menu> getAllActual() {
        return super.getAllActual();
    }

    @Override
    @GetMapping(MENUS_URL)
    public List<Menu> getAll(@Nullable @RequestParam(value = "date") LocalDate date,
                             @Nullable @RequestParam(value = "restaurantId") Integer id) {
        return super.getAll(date, id);
    }
}