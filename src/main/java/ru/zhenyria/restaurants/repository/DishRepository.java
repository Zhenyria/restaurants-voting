package ru.zhenyria.restaurants.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.zhenyria.restaurants.model.Dish;

@Repository
@Transactional(readOnly = true)
public interface DishRepository extends JpaRepository<Dish, Integer> {

    @Query(value = "SELECT COUNT(*) FROM MENUS_DISHES WHERE DISH_ID=:id", nativeQuery = true)
    int countUsing(@Param("id") int id);

    @Transactional
    @Modifying
    @Query(value = """
            INSERT INTO MENUS_DISHES (DISH_ID, MENU_ID) 
            VALUES (:id, :menuId)
            """, nativeQuery = true)
    int addToMenu(@Param("menuId") int menuId, @Param("id") int id);

    @Transactional
    @Modifying
    @Query(value = """
            DELETE FROM MENUS_DISHES WHERE MENU_ID=:menuId AND DISH_ID=:id
            """, nativeQuery = true)
    int deleteFromMenu(@Param("menuId") int menuId, @Param("id") int id);

    @Transactional
    @Modifying
    @Query("DELETE FROM Dish d WHERE d.id=:id")
    int delete(@Param("id") int id);
}
