package ru.zhenyria.restaurants.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "menus")
public class Menu extends AbstractBaseEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurant_id", nullable = false, updatable = false)
    @NotNull
    private Restaurant restaurant;

    @Column(name = "date", nullable = false, updatable = false)
    @NotNull
    private LocalDate date = LocalDate.now();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "menus_dishes",
            joinColumns = @JoinColumn(name = "menu_id"),
            inverseJoinColumns = @JoinColumn(name = "dish_id")
    )
    @OrderBy("name, price ASC")
    private Set<Dish> dishes;

    public Menu() {
    }

    public Menu(Menu menu) {
        this(menu.getId(), menu.getRestaurant(), menu.getDate(), menu.getDishes());
    }

    public Menu(Restaurant restaurant, LocalDate date, Set<Dish> dishes) {
        this(null, restaurant, date, dishes);
    }

    public Menu(Integer id, Restaurant restaurant, Set<Dish> dishes) {
        this(id, restaurant, LocalDate.now(), dishes);
    }

    public Menu(Integer id, Restaurant restaurant, LocalDate date, Set<Dish> dishes) {
        super(id);
        this.restaurant = restaurant;
        this.date = date;
        this.dishes = dishes;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public Set<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(Set<Dish> dishes) {
        this.dishes = dishes;
    }

    @Override
    public String toString() {
        return "Menu{" +
               "id=" + id +
               ", restaurant=" + restaurant +
               ", date=" + date +
               ", dishes=" + dishes +
               '}';
    }
}
