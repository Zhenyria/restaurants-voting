package ru.zhenyria.restaurants.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.zhenyria.restaurants.model.User;
import ru.zhenyria.restaurants.repository.UserRepository;
import ru.zhenyria.restaurants.to.UserTo;

import java.util.List;

import static ru.zhenyria.restaurants.util.UserUtil.prepareToSave;
import static ru.zhenyria.restaurants.util.UserUtil.updateUserFromTo;
import static ru.zhenyria.restaurants.util.ValidationUtil.checkExisting;

@Service
public class UserService {
    private static final String NULL_USER_MSG = "User must be not null";
    private static final String NULL_EMAIL_MSG = "Email must be not null";

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public User get(int id) {
        return checkExisting(repository.get(id));
    }

    public User getByEmail(String email) {
        Assert.notNull(email, NULL_EMAIL_MSG);
        return checkExisting(repository.getByEmail(email));
    }

    public User create(User user) {
        Assert.notNull(user, NULL_USER_MSG);
        return checkExisting(prepareAndSave(user));
    }

    @Transactional
    public void update(UserTo updated) {
        Assert.notNull(updated, NULL_USER_MSG);
        User user = get(updated.id());
        checkExisting(prepareAndSave(updateUserFromTo(user, updated)));
    }

    public void update(User user) {
        Assert.notNull(user, NULL_USER_MSG);
        checkExisting(prepareAndSave(user));
    }

    public void delete(int id) {
        checkExisting(repository.delete(id));
    }

    public List<User> getAll() {
        return repository.getAll();
    }

    private User prepareAndSave(User user) {
        return repository.save(prepareToSave(user, passwordEncoder));
    }
}
