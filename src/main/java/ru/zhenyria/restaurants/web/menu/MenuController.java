package ru.zhenyria.restaurants.web.menu;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.zhenyria.restaurants.model.Menu;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = MenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class MenuController extends AbstractMenuController {
    static final String REST_URL = "rest/profile/restaurants";

    @Override
    @GetMapping("/menus/{id}")
    public Menu get(@PathVariable int id) {
        return super.get(id);
    }

    @Override
    @GetMapping("/{id}/menus/actual")
    public Menu getActual(@PathVariable int id) {
        return super.getActual(id);
    }

    @Override
    @GetMapping("/{id}/menus/{date}")
    public Menu getForRestaurantByDate(@PathVariable int id, @PathVariable LocalDate date) {
        return super.getForRestaurantByDate(id, date);
    }

    @Override
    @GetMapping("/menus/actual")
    public List<Menu> getAllActual() {
        return super.getAllActual();
    }

    @Override
    @GetMapping("/menus/{date}")
    public List<Menu> getAllByDate(@PathVariable LocalDate date) {
        return super.getAllByDate(date);
    }

    @Override
    @GetMapping("/{id}/menus")
    public List<Menu> getAllForRestaurant(@PathVariable int id) {
        return super.getAllForRestaurant(id);
    }

    @Override
    @GetMapping("/menus")
    public List<Menu> getAll() {
        return super.getAll();
    }
}