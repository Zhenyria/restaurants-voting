package ru.zhenyria.restaurants.to;

import ru.zhenyria.restaurants.model.Dish;

import javax.validation.constraints.NotNull;
import java.util.List;

public class MenuTo extends BaseTo {

    @NotNull
    private Integer restaurantId;

    @NotNull
    private List<Dish> dishes;

    public MenuTo() {
    }

    public MenuTo(Integer restaurantId, List<Dish> dishes) {
        this.restaurantId = restaurantId;
        this.dishes = dishes;
    }

    public MenuTo(Integer id, Integer restaurantId, List<Dish> dishes) {
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

    public List<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(List<Dish> dishes) {
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
