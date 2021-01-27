package ru.zhenyria.restaurants.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Range;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.Set;

@Entity
@Table(name = "dishes")
public class Dish extends AbstractNamedEntity {

    @Column(name = "price")
    @NotNull
    @Range(min = 1)
    private Integer price;

    @ManyToMany(mappedBy = "dishes")
    @JsonIgnore
    private Set<Menu> menus;

    public Dish() {
    }

    public Dish(Dish dish) {
        this(dish.getId(), dish.getName(), dish.getPrice(), dish.getMenus());
    }

    public Dish(Integer id, String name, Integer price) {
        this(id, name, price, Collections.emptySet());
    }

    public Dish(String name, Integer price, Set<Menu> menus) {
        this(null, name, price, menus);
    }

    public Dish(Integer id, String name, Integer price, Set<Menu> menus) {
        super(id, name);
        this.name = name;
        this.price = price;
        this.menus = menus;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Set<Menu> getMenus() {
        return menus;
    }

    public void setMenus(Set<Menu> menu) {
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
