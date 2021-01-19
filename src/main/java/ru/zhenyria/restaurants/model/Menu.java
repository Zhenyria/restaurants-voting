package ru.zhenyria.restaurants.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

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
    private List<Dish> dishes;

    @ManyToMany
    @JoinTable(
            name = "votes",
            joinColumns = @JoinColumn(name = "menu_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @OrderBy("name")
    @JsonIgnore
    private List<User> users;

    public Menu() {
    }

    public Menu(Integer id, Restaurant restaurant, List<Dish> dishes, List<User> users) {
        this(id, restaurant, LocalDate.now(), dishes, users);
    }

    public Menu(Menu menu) {
        this(menu.getId(), menu.getRestaurant(), menu.getDate(), menu.getDishes(), menu.getUsers());
    }

    public Menu(Restaurant restaurant, LocalDate date, List<Dish> dishes) {
        this(null, restaurant, date, dishes);
    }

    public Menu(Restaurant restaurant, LocalDate date, List<Dish> dishes, List<User> users) {
        this(null, restaurant, date, dishes, users);
    }

    public Menu(Integer id, Restaurant restaurant, LocalDate date, List<Dish> dishes) {
        this(id, restaurant, date, dishes, Collections.emptyList());
    }

    public Menu(Integer id, Restaurant restaurant, LocalDate date, List<Dish> dishes, List<User> users) {
        super(id);
        this.restaurant = restaurant;
        this.date = date;
        this.dishes = dishes;
        this.users = users;
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

    public List<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(List<Dish> dishes) {
        this.dishes = dishes;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
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
