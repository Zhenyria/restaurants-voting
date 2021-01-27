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
    static final String REST_URL = "/rest/restaurants/{id}/votes";
    private final Logger log = LoggerFactory.getLogger(getClass());
    protected final VoteService service;

    public VoteController(VoteService service) {
        this.service = service;
    }

    @GetMapping
    public int getVotesCount(@PathVariable int id, @Nullable @RequestParam(name = "date") LocalDate date) {
        log.info("count votes for restaurant {} by date {}", id, date);
        return service.getVotesCount(id, date);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void vote(@PathVariable int id, @AuthenticationPrincipal AuthorizedUser authUser) {
        log.info("vote by restaurant {}", id);
        service.vote(id, authUser.getId());
    }
}
