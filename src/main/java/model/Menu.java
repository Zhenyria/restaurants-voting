package model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "menus")
public class Menu extends AbstractBaseEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @NotNull
    private Restaurant restaurant;

    @Column(name = "date_time", nullable = false)
    @NotNull
    private LocalDateTime dateTime;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Dish> dishes;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Vote> votes;

    public Menu() {
    }

    public Menu(Restaurant restaurant, LocalDateTime dateTime, List<Dish> dishes, List<Vote> votes) {
        this(null, restaurant, dateTime, dishes, votes);
    }

    public Menu(Integer id, Restaurant restaurant, LocalDateTime dateTime, List<Dish> dishes, List<Vote> votes) {
        super(id);
        this.restaurant = restaurant;
        this.dateTime = dateTime;
        this.dishes = dishes;
        this.votes = votes;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public List<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(List<Dish> dishes) {
        this.dishes = dishes;
    }

    public List<Vote> getVotes() {
        return votes;
    }

    public void setVotes(List<Vote> votes) {
        this.votes = votes;
    }

    @Override
    public String toString() {
        return "Menu{" +
               "restaurant=" + restaurant +
               ", dateTime=" + dateTime +
               ", dishes=" + dishes +
               ", votes=" + votes +
               ", id=" + id +
               '}';
    }
}
