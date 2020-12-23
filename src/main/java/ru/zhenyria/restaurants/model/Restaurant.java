package ru.zhenyria.restaurants.model;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "restaurants")
public class Restaurant extends AbstractNamedEntity {

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    @OrderBy("date DESC")
    private List<Menu> menus;

    public Restaurant() {
    }

    public Restaurant(Restaurant restaurant) {
        this(restaurant.getId(), restaurant.getName(), restaurant.getMenus());
    }

    public Restaurant(String name) {
        this(null, name);
    }

    public Restaurant(Integer id, String name) {
        this(id, name, Collections.emptyList());
    }

    public Restaurant(String name, List<Menu> menus) {
        this(null, name, menus);
    }

    public Restaurant(Integer id, String name, List<Menu> menus) {
        super(id, name);
        this.menus = menus;
    }

    public List<Menu> getMenus() {
        return menus;
    }

    public void setMenus(List<Menu> menus) {
        this.menus = menus;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
               "id=" + id +
               ", name='" + name + '\'' +
               '}';
    }
}
