package ru.zhenyria.restaurants.repository;

import ru.zhenyria.restaurants.model.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface CrudMenuRepository extends JpaRepository<Menu, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Menu m WHERE m.id=:id")
    int delete(@Param("id") int id);

    @Query("SELECT m FROM Menu m WHERE m.date=:date ORDER BY m.restaurant.name ASC")
    List<Menu> getAllByDate(@Param("date") LocalDate date);

    @Query("SELECT m FROM Menu m WHERE m.restaurant.id=:id ORDER BY m.date DESC")
    List<Menu> getAllForRestaurant(@Param("id") int id);

    @Query("SELECT DISTINCT m FROM Menu m WHERE m.date=:date AND m.restaurant.id=:id")
    Menu getForRestaurantByDate(@Param("id") int id, @Param("date") LocalDate date);
}
