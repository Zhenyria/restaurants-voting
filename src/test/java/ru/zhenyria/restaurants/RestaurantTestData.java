package ru.zhenyria.restaurants;

import ru.zhenyria.restaurants.model.Restaurant;

import java.util.List;

import static ru.zhenyria.restaurants.TestMatcher.usingIgnoringFieldsComparator;

public class RestaurantTestData {
    public static final TestMatcher<Restaurant> RESTAURANT_MATCHER =
            usingIgnoringFieldsComparator(Restaurant.class, "menus", "users");
    public static final TestMatcher<Integer> VOTE_MATCHER = TestMatcher.usingEqualsComparator(Integer.class);

    public static final int FIRST_RESTAURANT_ID = 100004;

    public static final int FIRST_RESTAURANTS_ACTUAL_COUNTS = 0;
    public static final int SECOND_RESTAURANTS_ACTUAL_COUNTS = 2;
    public static final int THIRD_RESTAURANTS_ACTUAL_COUNTS = 1;
    public static final int FIRST_RESTAURANT_COUNT_BY_2020_12_01 = 2;
    public static final int FIRST_RESTAURANT_COUNT_BY_2020_12_02 = 0;
    public static final int FIRST_RESTAURANT_COUNT_BY_2020_12_03 = 2;

    public static final Restaurant restaurant1 = new Restaurant(FIRST_RESTAURANT_ID, "Goldy");
    public static final Restaurant restaurant2 = new Restaurant(FIRST_RESTAURANT_ID + 1, "Siberian eggs");
    public static final Restaurant restaurant3 = new Restaurant(FIRST_RESTAURANT_ID + 2, "Moscow Palace 1992");

    public static final Restaurant newRestaurant = new Restaurant("Trump Tower Restaurant");

    public static final List<Restaurant> restaurants = List.of(restaurant1, restaurant3, restaurant2);

    private RestaurantTestData() {
    }

    public static Restaurant getNew() {
        return new Restaurant(newRestaurant);
    }

    public static Restaurant getUpdated() {
        Restaurant restaurant = new Restaurant(restaurant1);
        restaurant.setName("Jhon F.K club");
        return restaurant;
    }
}
