package ru.zhenyria.restaurants.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.zhenyria.restaurants.model.Restaurant;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface CrudRestaurantRepository extends JpaRepository<Restaurant, Integer> {

    @Query(value = """
            SELECT DISTINCT * FROM RESTAURANTS WHERE ID IN 
            (SELECT RESTAURANT_ID FROM (
                     SELECT RESTAURANT_ID, COUNT(*) AS REST_COUNT
                     FROM VOTES AS VOTE, MENUS AS MENU, RESTAURANTS AS RESTAURANT
                     WHERE VOTE.MENU_ID=MENU.ID AND MENU.RESTAURANT_ID=RESTAURANT.ID AND MENU.DATE=:date
                     GROUP BY RESTAURANT_ID ORDER BY REST_COUNT DESC LIMIT 1))
                        """, nativeQuery = true)
    Restaurant getWinnerByDate(@Param("date") LocalDate date);

    @Query(value = """
            SELECT COUNT(*) FROM VOTES AS VOTE
            WHERE EXISTS(SELECT * FROM MENUS WHERE ID=VOTE.MENU_ID AND DATE=:date AND RESTAURANT_ID=:id)
            """, nativeQuery = true)
    int getVotesCountByDate(@Param("id") int id, @Param("date") LocalDate date);

    @Query(value = "SELECT COUNT(*) FROM VOTES WHERE USER_ID=:id AND DATE=TODAY()", nativeQuery = true)
    int countUserVotesToday(@Param("id") int id);

    @Transactional
    @Modifying
    @Query(value = """
            INSERT INTO VOTES (USER_ID, MENU_ID)
            VALUES (:userId, (SELECT DISTINCT MENU.ID
                              FROM MENUS as MENU
                              WHERE MENU.RESTAURANT_ID = :id
                                AND MENU.DATE = TODAY()))
            """, nativeQuery = true)
    void vote(@Param("id") int id, @Param("userId") int userId);

    @Transactional
    @Modifying
    @Query(value = """
            UPDATE VOTES
            SET MENU_ID = (SELECT DISTINCT MENU.ID
                           FROM MENUS as MENU
                           WHERE MENU.RESTAURANT_ID = :id
                             AND MENU.DATE = TODAY())
            WHERE USER_ID = :userId
              AND DATE = TODAY()
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
