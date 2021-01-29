package ru.zhenyria.restaurants.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zhenyria.restaurants.repository.RestaurantRepository;
import ru.zhenyria.restaurants.util.VoteUtil;

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
    public int getVotesCount(int id, LocalDate date) {
        checkExisting(repository.existsById(id));
        return repository.getVotesCountByDate(id, date == null ? LocalDate.now() : date);
    }

    @CacheEvict(value = "votes", allEntries = true)
    @Transactional
    public void vote(int id, int userId) {
        if (repository.isVotedToday(userId).isEmpty()) {
            repository.vote(id, userId);
        } else {
            if (isCanReVote()) {
                repository.reVote(id, userId);
            } else {
                throw new UnsupportedOperationException(
                        String.format("Re-voting after %d hours is impossible", VoteUtil.DEADLINE_HOURS));
            }
        }
    }
}
