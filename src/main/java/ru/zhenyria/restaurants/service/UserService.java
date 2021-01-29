package ru.zhenyria.restaurants.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Sort;
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
    private static final String NULL_PASSWORD_MSG = "Password must be not null";
    private static final Sort SORT_NAME = Sort.by(Sort.Direction.ASC, "name");

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public User get(int id) {
        return checkExisting(repository.getById(id));
    }

    @Cacheable("users")
    public User getByEmail(String email) {
        Assert.notNull(email, NULL_EMAIL_MSG);
        return checkExisting(repository.getByEmail(email));
    }

    @Caching(evict = {
            @CacheEvict(value = "usersList", allEntries = true),
            @CacheEvict(value = "users", allEntries = true)
    })
    public User create(User user) {
        Assert.notNull(user.getPassword(), NULL_PASSWORD_MSG);
        Assert.notNull(user, NULL_USER_MSG);
        return checkExisting(prepareAndSave(user));
    }

    @Caching(evict = {
            @CacheEvict(value = "usersList", allEntries = true),
            @CacheEvict(value = "users", allEntries = true)
    })
    @Transactional
    public void update(UserTo updated) {
        Assert.notNull(updated, NULL_USER_MSG);
        User user = get(updated.id());
        checkExisting(prepareAndUpdate(updateUserFromTo(user, updated)));
    }

    @Caching(evict = {
            @CacheEvict(value = "usersList", allEntries = true),
            @CacheEvict(value = "users", allEntries = true)
    })
    @Transactional
    public void update(User user) {
        Assert.notNull(user, NULL_USER_MSG);
        checkExisting(prepareAndUpdate(user));
    }

    @Caching(evict = {
            @CacheEvict(value = "usersList", allEntries = true),
            @CacheEvict(value = "users", allEntries = true)
    })
    public void delete(int id) {
        checkExisting(repository.delete(id) != 0);
    }

    @Cacheable("usersList")
    public List<User> getAll() {
        return repository.findAll(SORT_NAME);
    }

    private User prepareAndUpdate(User user) {
        if (user.getPassword() == null) {
            user.setPassword(repository.getPassword(user.id()));
            return repository.save(user);
        }
        return prepareAndSave(user);
    }

    private User prepareAndSave(User user) {
        return repository.save(prepareToSave(user, passwordEncoder));
    }
}
