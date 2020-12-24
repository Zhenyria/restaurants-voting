package ru.zhenyria.restaurants.to;

import ru.zhenyria.restaurants.HasId;

import java.io.Serializable;

public abstract class BaseTo implements HasId, Serializable {
    protected Integer id;

    public BaseTo() {
    }

    public BaseTo(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
