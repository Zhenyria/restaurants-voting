package ru.zhenyria.restaurants;

import ru.zhenyria.restaurants.model.Dish;
import ru.zhenyria.restaurants.model.Menu;
import ru.zhenyria.restaurants.to.MenuTo;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.zhenyria.restaurants.DishTestData.*;
import static ru.zhenyria.restaurants.RestaurantTestData.*;
import static ru.zhenyria.restaurants.util.MenuUtil.getToFromMenu;

public class MenuTestData {
    public static final TestMatcher<Menu> MENU_MATCHER =
            TestMatcher.usingAssertions(Menu.class,
                    (a, b) -> assertThat(a).usingRecursiveComparison()
                            .ignoringFields("restaurant.menus", "dishes.menus", "users").isEqualTo(b),
                    (a, b) -> assertThat(a).usingRecursiveComparison()
                            .ignoringFields("restaurant.menus", "dishes.menus", "users").isEqualTo(b));

    public static final TestMatcher<Menu> MENU_MATCHER_WITHOUT_DATE =
            TestMatcher.usingIgnoringFieldsComparator(
                    Menu.class, "restaurant.menus", "dishes.menus", "users", "date");

    public static final Integer FIRST_MENU_ID = 100007;
    public static final LocalDate DATE_12_01 = LocalDate.of(2020, 12, 1);
    public static final LocalDate DATE_12_02 = LocalDate.of(2020, 12, 2);
    public static final LocalDate DATE_12_03 = LocalDate.of(2020, 12, 3);
    public static final LocalDate YESTERDAY = LocalDate.now().minusDays(1);

    // menus for restaurant 1
    public static final Menu menu1 = new Menu(FIRST_MENU_ID, restaurant1, DATE_12_01, sortDishes(dish1, dish3, dish4, dish8, dish10));
    public static final Menu menu2 = new Menu(FIRST_MENU_ID + 1, restaurant1, DATE_12_02, sortDishes(dish2, dish4));
    public static final Menu menu3 = new Menu(FIRST_MENU_ID + 2, restaurant1, DATE_12_03, sortDishes(dish8, dish5, dish4, dish3));
    public static final Menu yesterdayMenu1 = new Menu(FIRST_MENU_ID + 21, restaurant1, YESTERDAY, Collections.emptyList());

    // menus for restaurant 2
    public static final Menu menu4 = new Menu(FIRST_MENU_ID + 3, restaurant2, DATE_12_01, sortDishes(dish1, dish4, dish5, dish7, dish10));
    public static final Menu menu5 = new Menu(FIRST_MENU_ID + 4, restaurant2, DATE_12_02, sortDishes(dish11, dish7, dish8, dish1, dish4));
    public static final Menu actualMenu1 = new Menu(FIRST_MENU_ID + 7, restaurant2, LocalDate.now(), sortDishes(dish3, dish6, dish7, dish9));
    public static final Menu yesterdayMenu2 = new Menu(FIRST_MENU_ID + 22, restaurant2, YESTERDAY, Collections.emptyList());

    // menus for restaurant 3
    public static final Menu menu6 = new Menu(FIRST_MENU_ID + 5, restaurant3, DATE_12_01, sortDishes(dish4, dish5, dish7, dish11));
    public static final Menu menu7 = new Menu(FIRST_MENU_ID + 6, restaurant3, DATE_12_02, sortDishes(dish6, dish3, dish4, dish5, dish9, dish11));
    public static final Menu actualMenu2 = new Menu(FIRST_MENU_ID + 8, restaurant3, LocalDate.now(), sortDishes(dish4, dish6, dish2, dish9));
    public static final Menu yesterdayMenu3 = new Menu(FIRST_MENU_ID + 23, restaurant3, YESTERDAY, Collections.emptyList());

    private MenuTestData() {
    }

    public static Menu getNew() {
        Menu menu = getUpdated();
        menu.setId(null);
        menu.setDate(LocalDate.now());
        return menu;
    }

    public static MenuTo getNewTo() {
        return getToFromMenu(getNew());
    }

    public static Menu getUpdated() {
        Menu menu = new Menu(menu1);
        menu.setDishes(sortDishes(dish1, dish2, dish3));
        return menu;
    }

    public static MenuTo getUpdatedTo() {
        return getToFromMenu(getUpdated());
    }

    public static Menu getWithAddedDish() {
        Menu menu = new Menu(menu1);
        menu.setDishes(sortDishes(dish1, dish2, dish3, dish4, dish8, dish10));
        return menu;
    }

    public static Menu getWithoutDeletedDish() {
        Menu menu = new Menu(menu1);
        menu.setDishes(sortDishes(dish3, dish4, dish8, dish10));
        return menu;
    }

    private static List<Dish> sortDishes(Dish... dishes) {
        return Arrays.stream(dishes)
                .sorted(Comparator.comparing(Dish::getName).thenComparing(Dish::getPrice))
                .collect(Collectors.toList());
    }
}
