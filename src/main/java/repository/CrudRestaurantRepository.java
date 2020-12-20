package repository;

import model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface CrudRestaurantRepository extends JpaRepository<Restaurant, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Restaurant r WHERE r.id=:id")
    int delete(@Param("id") int id);

    @Query("""
            SELECT DISTINCT r FROM Restaurant r LEFT JOIN FETCH Menu m 
            ON r.id=:id AND m.date=:date AND m.restaurant.id=:id
            """)
    Restaurant getWithMenu(@Param("id") int id, @Param("date") LocalDate date);

    @Query("SELECT r FROM Restaurant r LEFT JOIN FETCH Menu m ON m.date=:date")
    List<Restaurant> getAllWithMenu(@Param("date") LocalDate date);

    @Query("""
            SELECT r FROM Restaurant r WHERE NOT EXISTS 
            (SELECT r FROM Menu m WHERE m.restaurant.id=r.id AND m.date=:date)
            """)
    List<Restaurant> getAllWithoutMenu(@Param("date") LocalDate date);
}
