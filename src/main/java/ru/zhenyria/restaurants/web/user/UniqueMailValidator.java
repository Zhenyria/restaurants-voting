package ru.zhenyria.restaurants.web.user;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import ru.zhenyria.restaurants.model.User;
import ru.zhenyria.restaurants.repository.UserRepository;
import ru.zhenyria.restaurants.web.HasEmail;

@Component
public class UniqueMailValidator implements org.springframework.validation.Validator {

    private final UserRepository repository;

    public UniqueMailValidator(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return HasEmail.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        HasEmail user = ((HasEmail) target);
        if (StringUtils.hasText(user.getEmail())) {
            User dbUser = repository.getByEmail(user.getEmail().toLowerCase());
            if (dbUser != null && !dbUser.getId().equals(user.getId())) {
                errors.rejectValue("email", "error.duplicate.email");
            }
        }
    }
}
