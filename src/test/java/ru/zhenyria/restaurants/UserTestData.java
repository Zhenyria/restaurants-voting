package ru.zhenyria.restaurants;

import ru.zhenyria.restaurants.model.Role;
import ru.zhenyria.restaurants.model.User;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class UserTestData {
    public static final TestMatcher<User> USER_MATCHER = TestMatcher.usingIgnoringFieldsComparator(User.class, "menus", "registered");

    public static final int USER_ID = 100000;
    public static final int ADMIN_ID = 100003;
    public static final int NOT_FOUND_ID = 1;
    public static final String USER_EMAIL = "piter@gmail.com";

    public static final User user1 = new User(USER_ID, "Piter", "password", USER_EMAIL, Role.USER);
    public static final User user2 = new User(USER_ID + 1, "Nikolas", "password", "nikolas@gmail.com", Role.USER);
    public static final User user3 = new User(USER_ID + 2, "Petr", "password", "petr@mail.ru", Role.USER);
    public static final User admin = new User(ADMIN_ID, "Admin", "admin", "admin@gmail.com", Role.USER, Role.ADMIN);

    public static User getNew() {
        User user = new User(user1);
        user.setId(null);
        user.setName("Sonny");
        user.setEmail("gofman@yandex.ru");
        return user;
    }

    public static User getUpdated() {
        User user = new User(user1);
        user.setName("Petrenko");
        user.setEmail("my_email@rabler.ru");
        user.setPassword("newPassword");
        user.setRoles(EnumSet.of(Role.ADMIN, Role.USER));
        return user;
    }

    private UserTestData() {
    }
}
