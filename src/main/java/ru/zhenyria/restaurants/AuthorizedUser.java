package ru.zhenyria.restaurants;

import ru.zhenyria.restaurants.model.User;
import ru.zhenyria.restaurants.to.UserTo;
import ru.zhenyria.restaurants.util.UserUtil;

public class AuthorizedUser extends org.springframework.security.core.userdetails.User {
    private UserTo userTo;

    public AuthorizedUser(User user) {
        super(user.getEmail(), user.getPassword(), true, true, true, true, user.getRoles());
        this.userTo = UserUtil.getToFromUser(user);
    }

    public int getId() {
        return userTo.id();
    }

    public void update(UserTo newTo) {
        userTo = newTo;
    }

    public UserTo getUserTo() {
        return userTo;
    }

    @Override
    public String toString() {
        return userTo.toString();
    }
}