package ru.zhenyria.restaurants.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zhenyria.restaurants.repository.RestaurantRepository;
import ru.zhenyria.restaurants.util.VoteUtil;
import ru.zhenyria.restaurants.util.exception.VotingException;

import java.time.LocalDate;

import static ru.zhenyria.restaurants.util.ValidationUtil.checkExisting;
import static ru.zhenyria.restaurants.util.VoteUtil.isCanReVote;

@Service
public class VoteService {
    private final RestaurantRepository repository;

    public VoteService(RestaurantRepository repository) {
        this.repository = repository;
    }

    @Cacheable("votes")
    public int getVotesCount(int restaurantId, LocalDate date) {
        checkExisting(repository.existsById(restaurantId));
        return repository.getVotesCountByDate(restaurantId, date == null ? LocalDate.now() : date);
    }

    @CacheEvict(value = "votes", allEntries = true)
    @Transactional
    public void vote(int restaurantId, int userId) {
        if (repository.isVotedToday(userId).isEmpty()) {
            repository.vote(restaurantId, userId);
        } else {
            if (isCanReVote()) {
                repository.reVote(restaurantId, userId);
            } else {
                throw new VotingException(
                        String.format("Re-voting after %d hours is impossible", VoteUtil.DEADLINE_HOURS));
            }
        }
    }
}
