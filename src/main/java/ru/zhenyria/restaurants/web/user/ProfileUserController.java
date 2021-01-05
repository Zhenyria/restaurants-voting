package ru.zhenyria.restaurants.web.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.zhenyria.restaurants.AuthorizedUser;
import ru.zhenyria.restaurants.model.User;
import ru.zhenyria.restaurants.to.UserTo;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping(value = ProfileUserController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class ProfileUserController extends AbstractUserController {
    static final String REST_URL = "/rest/profile";

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> register(@Valid @RequestBody UserTo userTo) {
        User created = super.createFromTo(userTo);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL).build().toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @GetMapping
    public User get(@AuthenticationPrincipal AuthorizedUser authUser) {
        return super.get(authUser.getId());
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody UserTo user, @AuthenticationPrincipal AuthorizedUser authUser) throws BindException {
        validateBeforeUpdate(user, authUser.getId());
        super.update(user);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal AuthorizedUser authUser) {
        super.delete(authUser.getId());
    }
}
