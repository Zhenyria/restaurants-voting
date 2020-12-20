package model;

import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "users")
public class User extends AbstractNamedEntity {
    @Column(name = "password", nullable = false)
    @NotBlank
    @Size(min = 6, max = 30)
    private String password;

    @Column(name = "email", nullable = false)
    @Email
    @NotBlank
    private String email;

    @Column(name = "registered", nullable = false)
    @NotNull
    private LocalDateTime registered;

    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"),
            uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "role"}, name = "user_roles_unique_idx")})
    @Column(name = "role")
    @ElementCollection(fetch = FetchType.EAGER)
    @BatchSize(size = 200)
    private List<Role> roles;

    @ManyToMany(mappedBy = "users", fetch = FetchType.LAZY)
    private List<Menu> menus;

    public User() {
    }

    public User(String name, String password, String email, LocalDateTime registered, List<Role> roles, List<Menu> menus) {
        this(null, name, password, email, registered, roles, menus);
    }

    public User(Integer id, String name, String password, String email, LocalDateTime registered, List<Role> roles, List<Menu> menus) {
        super(id, name);
        this.name = name;
        this.password = password;
        this.email = email;
        this.registered = registered;
        this.roles = roles;
        this.menus = menus;
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

    public List<Role> getRoles() {
        return roles == null || roles.isEmpty() ? Collections.emptyList() : List.copyOf(roles);
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles == null || roles.isEmpty() ? Collections.emptyList() : List.copyOf(roles);
    }

    public List<Menu> getMenus() {
        return menus == null || menus.isEmpty() ? Collections.emptyList() : List.copyOf(menus);
    }

    public void setMenus(List<Menu> menus) {
        this.menus = menus == null || menus.isEmpty() ? Collections.emptyList() : List.copyOf(menus);
    }

    @Override
    public String toString() {
        return "User{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", password='" + password + '\'' +
               ", email='" + email + '\'' +
               ", registered=" + registered +
               ", roles=" + roles +
               ", menus=" + menus +
               '}';
    }
}
