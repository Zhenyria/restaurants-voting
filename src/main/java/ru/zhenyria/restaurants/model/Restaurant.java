package ru.zhenyria.restaurants.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "restaurants")
public class Restaurant extends AbstractNamedEntity {

    @OneToMany(mappedBy = "restaurant")
    @OrderBy("date DESC")
    @JsonIgnore
    private List<Menu> menus;

    @ManyToMany
    @JoinTable(
            name = "votes",
            joinColumns = @JoinColumn(name = "restaurant_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @OrderBy("name")
    @JsonIgnore
    private Set<User> users;

    public Restaurant() {
    }

    public Restaurant(Restaurant restaurant) {
        this(restaurant.getId(), restaurant.getName(), restaurant.getMenus(), restaurant.getUsers());
    }

    public Restaurant(String name) {
        this(null, name);
    }

    public Restaurant(Integer id, String name) {
        this(id, name, Collections.emptyList(), Collections.emptySet());
    }

    public Restaurant(String name, List<Menu> menus) {
        this(null, name, menus, Collections.emptySet());
    }

    public Restaurant(Integer id, String name, List<Menu> menus, Set<User> users) {
        super(id, name);
        this.menus = menus;
        this.users = users;
    }

    public List<Menu> getMenus() {
        return menus;
    }

    public void setMenus(List<Menu> menus) {
        this.menus = menus;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
               "id=" + id +
               ", name='" + name + '\'' +
               '}';
    }
}
