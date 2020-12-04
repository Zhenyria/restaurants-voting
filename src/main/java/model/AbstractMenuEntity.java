package model;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

@MappedSuperclass
public abstract class AbstractMenuEntity extends AbstractBaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    @NotNull
    protected Menu menu;

    public AbstractMenuEntity() {
    }

    public AbstractMenuEntity(Integer id, Menu menu) {
        super(id);
        this.menu = menu;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    @Override
    public String toString() {
        return "AbstractMenuEntity{" +
               "id=" + id +
               ", menu=" + menu +
               '}';
    }
}
