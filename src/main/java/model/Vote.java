package model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "votes")
public class Vote extends AbstractMenuEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    @Column(name = "date", nullable = false)
    @NotNull
    private LocalDate date;

    public Vote() {
    }

    public Vote(User user, Menu menu, LocalDate date) {
        this(null, user, menu, date);
    }

    public Vote(Integer id, User user, Menu menu, LocalDate date) {
        super(id, menu);
        this.user = user;
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Vote{" +
               "id=" + id +
               ", menu=" + menu +
               ", user=" + user +
               ", date=" + date +
               '}';
    }
}
