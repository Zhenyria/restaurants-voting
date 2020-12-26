package ru.zhenyria.restaurants;

import ru.zhenyria.restaurants.model.Dish;

import java.util.List;

public class DishTestData {
    public static final TestMatcher<Dish> DISH_MATCHER =
            TestMatcher.usingIgnoringFieldsComparator(Dish.class, "menus");

    public static final Integer FIRST_DISH_ID = 100016;
    public static final String FIRST_DISH_NAME = "Beef";
    public static final Integer FIRST_DISH_PRICE = 154;
    public static final Integer NOT_USING_DISH_ID = FIRST_DISH_ID + 11;

    public static final Dish dish1 = new Dish(FIRST_DISH_ID, FIRST_DISH_NAME, FIRST_DISH_PRICE);
    public static final Dish dish2 = new Dish(FIRST_DISH_ID + 1, "Cola", 46);
    public static final Dish dish3 = new Dish(FIRST_DISH_ID + 2, "Zero Cola", 47);
    public static final Dish dish4 = new Dish(FIRST_DISH_ID + 3, "Fish soup", 118);
    public static final Dish dish5 = new Dish(FIRST_DISH_ID + 4, "Eggs", 24);
    public static final Dish dish6 = new Dish(FIRST_DISH_ID + 5, "Coffee", 36);
    public static final Dish dish7 = new Dish(FIRST_DISH_ID + 6, "Meat", 118);
    public static final Dish dish8 = new Dish(FIRST_DISH_ID + 7, "Beer", 52);
    public static final Dish dish9 = new Dish(FIRST_DISH_ID + 8, "Pina Coladas", 106);
    public static final Dish dish10 = new Dish(FIRST_DISH_ID + 9, "Corny flakes", 60);
    public static final Dish dish11 = new Dish(FIRST_DISH_ID + 10, "Hamburger", 103);

    public static final Dish dish12 = new Dish(NOT_USING_DISH_ID, "Sprite", 23);

    public static final List<Dish> dishes =
            List.of(dish1, dish8, dish6, dish2, dish10, dish5, dish4, dish11, dish7, dish9, dish12, dish3);

    private DishTestData() {
    }

    public static Dish getNew() {
        Dish dish = new Dish(dish1);
        dish.setId(null);
        dish.setName("Pizza");
        dish.setPrice(183);
        return dish;
    }

    public static Dish getUpdated() {
        Dish dish = getNew();
        dish.setId(FIRST_DISH_ID);
        return dish;
    }
}
