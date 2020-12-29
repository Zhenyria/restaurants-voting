package ru.zhenyria.restaurants.util;

import ru.zhenyria.restaurants.model.User;
import ru.zhenyria.restaurants.to.UserTo;

public class UserUtil {
    private UserUtil() {
    }

    public static User updateUserFromTo(User user, UserTo updated) {
        user.setName(updated.getName());
        user.setEmail(updated.getEmail());
        user.setPassword(updated.getPassword());
        return user;
    }
}
