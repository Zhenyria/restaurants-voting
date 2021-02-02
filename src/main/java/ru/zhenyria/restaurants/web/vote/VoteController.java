package ru.zhenyria.restaurants.web.vote;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.zhenyria.restaurants.AuthorizedUser;
import ru.zhenyria.restaurants.service.VoteService;

import java.time.LocalDate;

@RestController
@RequestMapping(value = VoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class VoteController {
    static final String REST_URL = "/rest/restaurants/{restaurantId}/votes";
    private final Logger log = LoggerFactory.getLogger(getClass());
    protected final VoteService service;

    public VoteController(VoteService service) {
        this.service = service;
    }

    @GetMapping
    public int getVotesCount(@PathVariable int restaurantId, @Nullable @RequestParam(name = "date") LocalDate date) {
        log.info("count votes for restaurant {} by date {}", restaurantId, date);
        return service.getVotesCount(restaurantId, date);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void vote(@PathVariable int restaurantId, @AuthenticationPrincipal AuthorizedUser authUser) {
        log.info("vote by restaurant {}", restaurantId);
        service.vote(restaurantId, authUser.getId());
    }
}
