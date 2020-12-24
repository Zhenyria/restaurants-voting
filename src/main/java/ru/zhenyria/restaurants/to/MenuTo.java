package ru.zhenyria.restaurants.to;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Arrays;

public class MenuTo extends BaseTo {

    @NotNull
    private Integer restaurantId;

    @NotNull
    private Integer[] dishIds;

    @NotNull
    private LocalDate date;

    public MenuTo() {
    }

    public MenuTo(Integer id, Integer restaurantId, Integer[] dishIds, LocalDate date) {
        super(id);
        this.restaurantId = restaurantId;
        this.dishIds = dishIds;
        this.date = date;
    }

    public Integer getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Integer restaurantId) {
        this.restaurantId = restaurantId;
    }

    public Integer[] getDishIds() {
        return dishIds;
    }

    public void setDishIds(Integer[] dishIds) {
        this.dishIds = dishIds;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "MenuTo{" +
               "id=" + id +
               ", restaurantId=" + restaurantId +
               ", dishIds=" + Arrays.toString(dishIds) +
               ", date=" + date +
               '}';
    }
}
