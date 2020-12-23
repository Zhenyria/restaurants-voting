package ru.zhenyria.restaurants.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
@Transactional(readOnly = true)
public class VoteRepository {

    @PersistenceContext
    private final EntityManager em;

    public VoteRepository(EntityManager em) {
        this.em = em;
    }
}
