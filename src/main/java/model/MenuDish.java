package model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "menu_dishes")
public class MenuDish extends AbstractMenuEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dish_id", nullable = false)
    @NotNull
    private Dish dish;

    public MenuDish() {
    }

    public MenuDish(Dish dish, Menu menu) {
        this(null, dish, menu);
    }

    public MenuDish(Integer id, Dish dish, Menu menu) {
        super(id, menu);
        this.dish = dish;
    }

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    @Override
    public String toString() {
        return "MenuDish{" +
               "id=" + id +
               ", menu=" + menu +
               ", dish=" + dish +
               '}';
    }
}
