package ru.zhenyria.restaurants.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.zhenyria.restaurants.model.Dish;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface DishRepository extends JpaRepository<Dish, Integer> {

    Dish getById(int id);

    /**
     * Determine if the current dish is in use or not
     *
     * @param id of current dish
     * @return Optional<Integer>. That Optional object has value 1 if current dish is in use
     * or null, if current dish is not in use
     */
    @Query(value = "SELECT 1 FROM MENUS_DISHES WHERE DISH_ID=:id LIMIT 1", nativeQuery = true)
    Optional<Integer> isUsed(@Param("id") int id);

    @Transactional
    @Modifying
    @Query("DELETE FROM Dish d WHERE d.id=:id")
    int delete(@Param("id") int id);
}
