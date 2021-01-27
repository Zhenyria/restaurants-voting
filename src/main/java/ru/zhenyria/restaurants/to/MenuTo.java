package ru.zhenyria.restaurants.to;

import ru.zhenyria.restaurants.model.Dish;

import javax.validation.constraints.NotNull;
import java.util.Set;

public class MenuTo extends BaseTo {

    @NotNull
    private Integer restaurantId;

    @NotNull
    private Set<Dish> dishes;

    public MenuTo() {
    }

    public MenuTo(Integer restaurantId, Set<Dish> dishes) {
        this.restaurantId = restaurantId;
        this.dishes = dishes;
    }

    public MenuTo(Integer id, Integer restaurantId, Set<Dish> dishes) {
        super(id);
        this.restaurantId = restaurantId;
        this.dishes = dishes;
    }

    public Integer getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Integer restaurantId) {
        this.restaurantId = restaurantId;
    }

    public Set<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(Set<Dish> dishes) {
        this.dishes = dishes;
    }

    @Override
    public String toString() {
        return "MenuTo{" +
               "id=" + id +
               ", restaurantId=" + restaurantId +
               ", dishes=" + dishes +
               '}';
    }
}
