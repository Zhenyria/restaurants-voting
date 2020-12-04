package model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "menu_dishes")
public class MenuDish extends AbstractBaseEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dish_id", nullable = false)
    @NotNull
    private Dish dish;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    @NotNull
    private Menu menu;

    public MenuDish() {
    }

    public MenuDish(Dish dish, Menu menu) {
        this(null, dish, menu);
    }

    public MenuDish(Integer id, Dish dish, Menu menu) {
        super(id);
        this.dish = dish;
        this.menu = menu;
    }

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    @Override
    public String toString() {
        return "MenuDish{" +
               "id=" + id +
               ", dish=" + dish +
               ", menu=" + menu +
               '}';
    }
}
