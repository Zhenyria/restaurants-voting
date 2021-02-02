package ru.zhenyria.restaurants.web.restaurant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.zhenyria.restaurants.View;
import ru.zhenyria.restaurants.model.Restaurant;
import ru.zhenyria.restaurants.service.RestaurantService;

import java.net.URI;
import java.util.List;

import static ru.zhenyria.restaurants.util.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = AdminRestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminRestaurantController {
    static final String REST_URL = "/rest/admin/restaurants";
    private final Logger log = LoggerFactory.getLogger(getClass());

    protected final RestaurantService service;

    public AdminRestaurantController(RestaurantService service) {
        this.service = service;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> createWithLocation(@Validated(View.Web.class) @RequestBody Restaurant restaurant) {
        log.info("create restaurant {}", restaurant);
        checkNew(restaurant);
        Restaurant created = service.create(restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.id()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @GetMapping
    public List<Restaurant> getAll() {
        log.info("get all restaurants");
        return service.getAll();
    }

    @GetMapping("/without_menu")
    public List<Restaurant> getAllWithoutActualMenu() {
        log.info("get all restaurants without actual menu");
        return service.getAllWithoutActualMenu();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void rename(@PathVariable int id, @RequestBody String name) {
        log.info("update restaurant {}", id);
        service.update(new Restaurant(id, name));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete restaurant {}", id);
        service.delete(id);
    }
}
