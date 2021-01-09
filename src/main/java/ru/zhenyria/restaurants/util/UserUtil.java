package ru.zhenyria.restaurants.util;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import ru.zhenyria.restaurants.model.Role;
import ru.zhenyria.restaurants.model.User;
import ru.zhenyria.restaurants.to.UserTo;

public class UserUtil {
    private UserUtil() {
    }

    public static User updateUserFromTo(User user, UserTo updated) {
        user.setName(updated.getName());
        user.setEmail(updated.getEmail());
        if (updated.getPassword() != null) {
            user.setPassword(updated.getPassword());
        }
        return user;
    }

    public static User getUserFromTo(UserTo user) {
        return new User(user.getId(), user.getName(), user.getPassword(), user.getEmail().toLowerCase(), Role.USER);
    }

    public static UserTo getToFromUser(User user) {
        return new UserTo(user.getId(), user.getName(), user.getEmail(), user.getPassword());
    }

    public static User prepareToSave(User user, PasswordEncoder passwordEncoder) {
        String password = user.getPassword();
        user.setPassword(StringUtils.hasText(password) ? passwordEncoder.encode(password) : password);
        user.setEmail(user.getEmail().toLowerCase());
        return user;
    }
}
