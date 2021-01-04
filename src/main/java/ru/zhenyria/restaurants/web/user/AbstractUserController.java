package ru.zhenyria.restaurants.web.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.zhenyria.restaurants.model.User;
import ru.zhenyria.restaurants.service.UserService;
import ru.zhenyria.restaurants.to.UserTo;

import java.util.List;

import static ru.zhenyria.restaurants.util.UserUtil.getUserFromTo;
import static ru.zhenyria.restaurants.util.ValidationUtil.checkNew;

public abstract class AbstractUserController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    protected UserService service;

    public User get(int id) {
        log.info("get user {}", id);
        return service.get(id);
    }

    public List<User> getAll() {
        log.info("get all users");
        return service.getAll();
    }

    public User getByEmail(String email) {
        log.info("get user by email {}", email);
        return service.getByEmail(email);
    }

    public User create(User user) {
        log.info("create user {}", user);
        checkNew(user);
        return service.create(user);
    }

    public User createFromTo(UserTo user) {
        log.info("create user from to {}", user);
        return service.create(getUserFromTo(user));
    }

    public void update(UserTo user) {
        log.info("update user with id {}", user.id());
        service.update(user);
    }

    public void update(User user) {
        log.info("update user with id {} from to", user.id());
        service.update(user);
    }

    public void delete(int id) {
        log.info("delete user with id {}", id);
        service.delete(id);
    }
}
