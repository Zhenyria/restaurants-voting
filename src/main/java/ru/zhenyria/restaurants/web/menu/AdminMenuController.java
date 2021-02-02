package ru.zhenyria.restaurants.web.menu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.zhenyria.restaurants.model.Menu;
import ru.zhenyria.restaurants.service.MenuService;
import ru.zhenyria.restaurants.to.MenuTo;

import javax.validation.Valid;
import java.net.URI;

import static ru.zhenyria.restaurants.util.ValidationUtil.assureIdConsistent;
import static ru.zhenyria.restaurants.util.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = AdminMenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminMenuController {
    static final String REST_URL = "/rest/admin/menus";
    static final String DISHES_URL = "/dishes";
    private final Logger log = LoggerFactory.getLogger(getClass());

    protected final MenuService service;

    public AdminMenuController(MenuService service) {
        this.service = service;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Menu> createWithLocation(@Valid @RequestBody MenuTo menu) {
        log.info("create menu {} for restaurant {}", menu, menu.getRestaurantId());
        checkNew(menu);
        Menu created = service.create(menu);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.id()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable int id, @Valid @RequestBody MenuTo menu) {
        log.info("update menu {}", menu.id());
        assureIdConsistent(menu, id);
        service.update(menu);
    }

    @PutMapping("/{id}" + DISHES_URL + "/{dishId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addDish(@PathVariable int id, @PathVariable int dishId) {
        log.info("add dish {} to menu {}", dishId, id);
        service.addDish(id, dishId);
    }

    @DeleteMapping("/{id}" + DISHES_URL + "/{dishId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDish(@PathVariable int id, @PathVariable int dishId) {
        log.info("delete dish {} from menu {}", dishId, id);
        service.deleteDish(id, dishId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete menu {}", id);
        service.delete(id);
    }
}