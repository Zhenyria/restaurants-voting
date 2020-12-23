package ru.zhenyria.restaurants;

import ru.zhenyria.restaurants.model.Restaurant;

import java.util.List;

import static ru.zhenyria.restaurants.TestMatcher.usingIgnoringFieldsComparator;

public class RestaurantTestData {
    public static final TestMatcher<Restaurant> RESTAURANT_MATCHER =
            usingIgnoringFieldsComparator(Restaurant.class, "menus");

    public static final int FIRST_RESTAURANT_ID = 100004;

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
