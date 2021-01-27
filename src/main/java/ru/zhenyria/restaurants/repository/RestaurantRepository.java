package ru.zhenyria.restaurants.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.zhenyria.restaurants.model.Restaurant;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {

    Restaurant getById(int id);

    @Query(value = """
            SELECT DISTINCT * FROM restaurants WHERE id IN
                (SELECT restaurant_id FROM (
                    SELECT restaurant_id, COUNT(*) AS votes_count
                    FROM votes AS vote, restaurants AS restaurant
                    WHERE vote.restaurant_id=restaurant.id AND vote.date=:date
                    GROUP BY restaurant_id ORDER BY votes_count DESC LIMIT 1))
                        """, nativeQuery = true)
    Restaurant getWinnerByDate(@Param("date") LocalDate date);

    @Query(value = """
            SELECT COUNT(*) FROM votes AS vote
            WHERE vote.date=:date AND vote.restaurant_id=:id
            """, nativeQuery = true)
    int getVotesCountByDate(@Param("id") int id, @Param("date") LocalDate date);

    /**
     * Determine if the current user is voted today or not
     *
     * @param id of current user
     * @return Optional<Integer>. That Optional object has value 1 if current user voted
     * or null, if current user not voted today
     */
    @Query(value = "SELECT 1 FROM votes WHERE user_id=:id AND date=TODAY() LIMIT 1", nativeQuery = true)
    Optional<Integer> isVotedToday(@Param("id") int id);

    @Transactional
    @Modifying
    @Query(value = """
            INSERT INTO votes (USER_ID, RESTAURANT_ID)
            VALUES (:userId, :id)
            """, nativeQuery = true)
    void vote(@Param("id") int id, @Param("userId") int userId);

    @Transactional
    @Modifying
    @Query(value = """
            UPDATE votes
            SET restaurant_id=:id
            WHERE user_id=:userId AND date=TODAY()
            """, nativeQuery = true)
    void reVote(@Param("id") int id, @Param("userId") int userId);

    @Query("""
            SELECT r FROM Restaurant r WHERE EXISTS 
            (SELECT r FROM Menu m WHERE m.restaurant.id=r.id AND m.date=:date) 
            ORDER BY r.name
            """)
    List<Restaurant> getAllWithActualMenu(@Param("date") LocalDate date);

    @Query("""
            SELECT r FROM Restaurant r WHERE NOT EXISTS 
            (SELECT r FROM Menu m WHERE m.restaurant.id=r.id AND m.date=:date) 
            ORDER BY r.name
            """)
    List<Restaurant> getAllWithoutActualMenu(@Param("date") LocalDate date);

    @Transactional
    @Modifying
    @Query("DELETE FROM Restaurant r WHERE r.id=:id")
    int delete(@Param("id") int id);
}
