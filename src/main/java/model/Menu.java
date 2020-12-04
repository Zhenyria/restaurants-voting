package model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "menus")
public class Menu extends AbstractBaseEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @NotNull
    private Restaurant restaurant;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @OneToMany(fetch = FetchType.EAGER)
    private List<MenuDish> dishes;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Vote> votes;

    public Menu() {
    }

    public Menu(Restaurant restaurant, LocalDate date, List<MenuDish> dishes, List<Vote> votes) {
        this(null, restaurant, date, dishes, votes);
    }

    public Menu(Integer id, Restaurant restaurant, LocalDate date, List<MenuDish> dishes, List<Vote> votes) {
        super(id);
        this.restaurant = restaurant;
        this.date = date;
        this.dishes = dishes;
        this.votes = votes;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public LocalDate getDateTime() {
        return date;
    }

    public void setDateTime(LocalDate date) {
        this.date = date;
    }

    public List<MenuDish> getDishes() {
        return dishes;
    }

    public void setDishes(List<MenuDish> dishes) {
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
               "id=" + id +
               ", restaurant=" + restaurant +
               ", date=" + date +
               ", dishes=" + dishes +
               ", votes=" + votes +
               '}';
    }
}
