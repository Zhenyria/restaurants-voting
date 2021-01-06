package ru.zhenyria.restaurants.web.restaurant;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.zhenyria.restaurants.AuthorizedUser;
import ru.zhenyria.restaurants.model.Restaurant;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = RestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantController extends AbstractRestaurantController {
    static final String REST_URL = "rest/profile/restaurants";

    @Override
    @GetMapping("/{id}")
    public Restaurant get(@PathVariable int id) {
        return super.get(id);
    }

    @Override
    @GetMapping("/winner")
    public Restaurant getWinner() {
        return super.getWinner();
    }

    @Override
    @GetMapping("/winning")
    public Restaurant getWinning() {
        return super.getWinning();
    }

    @Override
    @GetMapping("/winner/{date}")
    public Restaurant getWinnerByDate(@PathVariable LocalDate date) {
        return super.getWinnerByDate(date);
    }

    @Override
    @GetMapping
    public List<Restaurant> getAllWithActualMenu() {
        return super.getAllWithActualMenu();
    }

    @Override
    @GetMapping("/{id}/rating")
    public int countVotes(@PathVariable int id) {
        return super.countVotes(id);
    }

    @Override
    @GetMapping("/{id}/rating/{date}")
    public int countVotesByDate(@PathVariable int id, @PathVariable LocalDate date) {
        return super.countVotesByDate(id, date);
    }

    @PostMapping("/{id}/vote")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void vote(@PathVariable int id, @AuthenticationPrincipal AuthorizedUser authUser) {
        super.vote(id, authUser.getId());
    }
}