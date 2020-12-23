package ru.zhenyria.restaurants.repository;

import ru.zhenyria.restaurants.model.Restaurant;
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
}
