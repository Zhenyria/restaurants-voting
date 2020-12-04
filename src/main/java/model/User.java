package model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "users")
public class User extends AbstractNamedEntity {
    private static final String emailRegex = "^([a-z0-9_-]+\\.)*[a-z0-9_-]+@[a-z0-9_-]+(\\.[a-z0-9_-]+)*\\.[a-z]{2,6}$";

    @Column(name = "password", nullable = false)
    @NotBlank
    @Size(min = 6, max = 30)
    private String password;

    @Column(name = "email", nullable = false)
    @NotBlank
    @Pattern(regexp = emailRegex)
    private String email;

    @Column(name = "registered", nullable = false)
    @NotNull
    private LocalDateTime registered;

    @Column(name = "role", nullable = false)
    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(fetch = FetchType.LAZY)
    @OrderBy("date DESC")
    private List<Vote> votes;

    public User() {
    }

    public User(String name, String password, String email, LocalDateTime registered, Role role, List<Vote> votes) {
        this(null, name, password, email, registered, role, votes);
    }

    public User(Integer id, String name, String password, String email, LocalDateTime registered, Role role, List<Vote> votes) {
        super(id, name);
        this.name = name;
        this.password = password;
        this.email = email;
        this.registered = registered;
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getRegistered() {
        return registered;
    }

    public void setRegistered(LocalDateTime registered) {
        this.registered = registered;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Vote> getVotes() {
        return votes == null || votes.isEmpty() ? Collections.emptyList() : List.copyOf(votes);
    }

    public void setVotes(List<Vote> votes) {
        this.votes = votes == null || votes.isEmpty() ? Collections.emptyList() : List.copyOf(votes);
    }

    @Override
    public String toString() {
        return "User{" +
               "name='" + name + '\'' +
               ", password='" + password + '\'' +
               ", email='" + email + '\'' +
               ", registered=" + registered +
               ", role=" + role +
               ", votes=" + votes +
               ", id=" + id +
               '}';
    }
}
