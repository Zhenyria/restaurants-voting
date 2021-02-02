package ru.zhenyria.restaurants.web.restaurant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import ru.zhenyria.restaurants.model.Restaurant;
import ru.zhenyria.restaurants.service.RestaurantService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = RestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantController {
    static final String REST_URL = "/rest/restaurants";
    private final Logger log = LoggerFactory.getLogger(getClass());

    protected final RestaurantService service;

    public RestaurantController(RestaurantService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public Restaurant get(@PathVariable int id) {
        log.info("get restaurant {}", id);
        return service.get(id);
    }

    @GetMapping("/winner")
    public Restaurant getWinner(@Nullable @RequestParam(value = "date") LocalDate date) {
        log.info("get restaurant-winner by date {}", date);
        return service.getWinner(date);
    }

    @GetMapping("/winning")
    public Restaurant getWinning() {
        log.info("get winning restaurant");
        return service.getWinning();
    }

    @GetMapping
    public List<Restaurant> getAllWithActualMenu() {
        log.info("get all restaurants with actual menu");
        return service.getAllWithActualMenu();
    }
}
