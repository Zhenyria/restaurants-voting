package ru.zhenyria.restaurants.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.zhenyria.restaurants.AuthorizedUser;
import ru.zhenyria.restaurants.model.User;

@Service("securityUserService")
public class SecurityUserService implements UserDetailsService {

    private final UserService service;

    public SecurityUserService(UserService service) {
        this.service = service;
    }

    @Override
    public AuthorizedUser loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = service.getByEmail(email.toLowerCase());
        if (user == null) {
            throw new UsernameNotFoundException("User " + email + " is not found");
        }
        return new AuthorizedUser(user);
    }
}
