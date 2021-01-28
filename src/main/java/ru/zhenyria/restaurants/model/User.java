package ru.zhenyria.restaurants.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.BatchSize;
import org.hibernate.validator.constraints.SafeHtml;
import ru.zhenyria.restaurants.HasEmail;
import ru.zhenyria.restaurants.View;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.*;

import static org.hibernate.validator.constraints.SafeHtml.WhiteListType.NONE;

@Entity
@Table(name = "users")
public class User extends AbstractNamedEntity implements HasEmail {
    @Column(name = "password", nullable = false)
    @Size(min = 6, max = 100)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(name = "email", nullable = false)
    @Email
    @NotBlank
    @SafeHtml(groups = {View.Web.class}, whitelistType = NONE)
    private String email;

    @Column(name = "registered", nullable = false)
    @NotNull
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime registered = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"),
            uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "role"}, name = "users_roles_unique_idx")})
    @Column(name = "role")
    @ElementCollection(fetch = FetchType.EAGER)
    @BatchSize(size = 200)
    private Set<Role> roles;

    public User() {
    }

    public User(User user) {
        this(user.getId(), user.getName(), user.getPassword(), user.getEmail(), user.getRoles());
    }

    public User(String name, String password, String email, Role... roles) {
        this(null, name, password, email, roles);
    }

    public User(Integer id, String name, String password, String email, Set<Role> roles) {
        this(id, name, password, email, roles.toArray(new Role[0]));
    }

    public User(String name, String password, String email, LocalDateTime registered, Set<Role> roles) {
        this(null, name, password, email, registered, roles);
    }

    public User(Integer id, String name, String password, String email, Role... roles) {
        this(id, name, password, email, LocalDateTime.now(), new HashSet<>(Arrays.asList(roles)));
    }

    public User(Integer id, String name, String password, String email, LocalDateTime registered, Set<Role> roles) {
        super(id, name);
        this.name = name;
        this.password = password;
        this.email = email;
        this.registered = registered;
        this.roles = roles;
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

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles == null || roles.isEmpty() ? Collections.emptySet() : EnumSet.copyOf(roles);
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
               '}';
    }
}
