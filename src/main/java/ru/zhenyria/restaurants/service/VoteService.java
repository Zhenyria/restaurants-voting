package ru.zhenyria.restaurants.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zhenyria.restaurants.repository.RestaurantRepository;

import java.time.LocalDate;

import static ru.zhenyria.restaurants.util.ValidationUtil.checkExisting;
import static ru.zhenyria.restaurants.util.VoteUtil.isCanReVote;

@Service
public class VoteService {
    private final RestaurantRepository repository;

    public VoteService(RestaurantRepository repository) {
        this.repository = repository;
    }

    public int getVotesCount(int id, LocalDate date) {
        checkExisting(repository.existsById(id));
        return repository.getVotesCountByDate(id, date == null ? LocalDate.now() : date);
    }

    @Transactional
    public void vote(int id, int userId) {
        if (repository.countUserVotesToday(userId) <= 0) {
            repository.vote(id, userId);
        } else {
            if (isCanReVote()) {
                repository.reVote(id, userId);
            } else {
                throw new UnsupportedOperationException("Re-voting after 11:00 is impossible");
            }
        }
    }
}
