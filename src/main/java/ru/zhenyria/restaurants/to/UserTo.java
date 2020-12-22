package ru.zhenyria.restaurants.to;

import org.hibernate.validator.constraints.SafeHtml;
import ru.zhenyria.restaurants.model.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

import static org.hibernate.validator.constraints.SafeHtml.WhiteListType.NONE;

public class UserTo extends NamedTo implements Serializable {

    @NotBlank
    @Email
    @SafeHtml(whitelistType = NONE)
    private String email;

    @NotBlank
    @Size(min = 6, max = 30)
    @SafeHtml(whitelistType = NONE)
    private String password;

    public UserTo() {
    }

    public UserTo(User user) {
        this(user.getId(), user.getName(), user.getEmail(), user.getPassword());
    }

    public UserTo(Integer id, String name, String email, String password) {
        super(id, name);
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserTo{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", email='" + email + '\'' +
               ", password='" + password + '\'' +
               '}';
    }
}
