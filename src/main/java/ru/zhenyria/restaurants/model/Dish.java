package ru.zhenyria.restaurants.model;

import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "dishes")
public class Dish extends AbstractNamedEntity {

    @Column(name = "price")
    @NotNull
    @Range(min = 1)
    private Integer price;

    @ManyToMany(mappedBy = "dishes", fetch = FetchType.LAZY)
    private List<Menu> menus;

    public Dish() {
    }

    public Dish(Dish dish) {
        this(dish.getId(), dish.getName(), dish.getPrice(), dish.getMenus());
    }

    public Dish(Integer id, String name, Integer price) {
        this(id, name, price, Collections.emptyList());
    }

    public Dish(String name, Integer price, List<Menu> menus) {
        this(null, name, price, menus);
    }

    public Dish(Integer id, String name, Integer price, List<Menu> menus) {
        super(id, name);
        this.name = name;
        this.price = price;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public List<Menu> getMenus() {
        return menus;
    }

    public void setMenus(List<Menu> menu) {
        this.menus = menu;
    }

    @Override
    public String toString() {
        return "Dish{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", price=" + price +
               '}';
    }
}
