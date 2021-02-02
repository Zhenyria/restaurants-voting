package ru.zhenyria.restaurants.web.dish;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.zhenyria.restaurants.model.Dish;
import ru.zhenyria.restaurants.service.DishService;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static ru.zhenyria.restaurants.util.ValidationUtil.assureIdConsistent;
import static ru.zhenyria.restaurants.util.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = DishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class DishController {
    static final String REST_URL = "/rest/admin/dishes";
    private final Logger log = LoggerFactory.getLogger(getClass());
    protected final DishService service;

    public DishController(DishService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Dish> createWithLocation(@RequestBody @Valid Dish dish) {
        log.info("create dish {}", dish);
        checkNew(dish);
        Dish created = service.create(dish);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(dish.id()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @GetMapping("/{id}")
    public Dish get(@PathVariable int id) {
        log.info("get dish {}", id);
        return service.get(id);
    }

    @GetMapping
    public List<Dish> getAll() {
        log.info("get all dishes");
        return service.getAll();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable int id, @Valid @RequestBody Dish dish) {
        assureIdConsistent(dish, id);
        log.info("update dish {}", id);
        service.update(dish);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete dish {}", id);
        service.delete(id);
    }
}