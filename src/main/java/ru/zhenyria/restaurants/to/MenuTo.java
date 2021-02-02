package ru.zhenyria.restaurants.to;

import javax.validation.constraints.NotNull;
import java.util.Set;

public class MenuTo extends BaseTo {

    @NotNull
    private Integer restaurantId;

    @NotNull
    private Set<Integer> dishIds;

    public MenuTo() {
    }

    public MenuTo(Integer restaurantId, Set<Integer> dishIds) {
        this.restaurantId = restaurantId;
        this.dishIds = dishIds;
    }

    public MenuTo(Integer id, Integer restaurantId, Set<Integer> dishIds) {
        super(id);
        this.restaurantId = restaurantId;
        this.dishIds = dishIds;
    }

    public Integer getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Integer restaurantId) {
        this.restaurantId = restaurantId;
    }

    public Set<Integer> getDishIds() {
        return dishIds;
    }

    public void setDishIds(Set<Integer> dishIds) {
        this.dishIds = dishIds;
    }

    @Override
    public String toString() {
        return "MenuTo{" +
               "id=" + id +
               ", restaurantId=" + restaurantId +
               ", dishIds=" + dishIds +
               '}';
    }
}
