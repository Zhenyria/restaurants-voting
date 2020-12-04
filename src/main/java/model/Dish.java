package model;

import org.hibernate.validator.constraints.Range;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "dishes")
public class Dish extends AbstractNamedEntity {

    @Column(name = "price")
    @NotNull
    @Range(min = 1)
    private Integer price;

    public Dish() {
    }

    public Dish(String name, Integer price) {
        this(null, name, price);
    }

    public Dish(Integer id, String name, Integer price) {
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

    @Override
    public String toString() {
        return "Dish{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", price=" + price +
               '}';
    }
}
