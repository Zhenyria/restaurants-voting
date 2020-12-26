package ru.zhenyria.restaurants.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.zhenyria.restaurants.model.Dish;

@Transactional(readOnly = true)
public interface CrudDishRepository extends JpaRepository<Dish, Integer> {

    @Query(value = "SELECT COUNT(*) FROM MENU_DISHES WHERE DISH_ID=:id", nativeQuery = true)
    int countUsing(@Param("id") int id);

    @Transactional
    @Modifying
    @Query(value = """
            INSERT INTO MENU_DISHES (DISH_ID, MENU_ID) 
            VALUES (:id, :menuId)
            """, nativeQuery = true)
    int addToMenu(@Param("menuId") int menuId, @Param("id") int id);

    @Transactional
    @Modifying
    @Query(value = """
            DELETE FROM MENU_DISHES WHERE MENU_ID=:menuId AND DISH_ID=:id
            """, nativeQuery = true)
    int deleteFormMenu(@Param("menuId") int menuId, @Param("id") int id);

    @Transactional
    @Modifying
    @Query("DELETE FROM Dish d WHERE d.id=:id")
    int delete(@Param("id") int id);
}
