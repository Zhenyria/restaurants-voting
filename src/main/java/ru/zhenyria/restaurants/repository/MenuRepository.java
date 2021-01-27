package ru.zhenyria.restaurants.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.zhenyria.restaurants.model.Menu;

import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface MenuRepository extends JpaRepository<Menu, Integer> {

    Menu getById(int id);

    @Transactional
    @Modifying
    @Query("DELETE FROM Menu m WHERE m.id=:id")
    int delete(@Param("id") int id);

    @Query("SELECT m FROM Menu m WHERE m.date=:date ORDER BY m.restaurant.name ASC")
    List<Menu> getAllByDate(@Param("date") LocalDate date);

    @Query("SELECT m FROM Menu m WHERE m.restaurant.id=:id ORDER BY m.date DESC")
    List<Menu> getAllByRestaurant(@Param("id") int id);

    @Query("SELECT DISTINCT m FROM Menu m WHERE m.date=:date AND m.restaurant.id=:id")
    Menu getByRestaurantAndDate(@Param("id") int id, @Param("date") LocalDate date);
}
