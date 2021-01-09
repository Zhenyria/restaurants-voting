package ru.zhenyria.restaurants.web.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.zhenyria.restaurants.model.Role;
import ru.zhenyria.restaurants.model.User;
import ru.zhenyria.restaurants.service.UserService;
import ru.zhenyria.restaurants.to.UserTo;
import ru.zhenyria.restaurants.util.exception.ErrorType;
import ru.zhenyria.restaurants.web.AbstractControllerTest;

import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.zhenyria.restaurants.TestUtil.readFromJson;
import static ru.zhenyria.restaurants.TestUtil.userHttpBasic;
import static ru.zhenyria.restaurants.UserTestData.*;
import static ru.zhenyria.restaurants.util.UserUtil.getToFromUser;
import static ru.zhenyria.restaurants.util.UserUtil.getUserFromTo;
import static ru.zhenyria.restaurants.web.ExceptionInfoHandler.EXCEPTION_DUPLICATE_EMAIL;

public class ProfileUserControllerTest extends AbstractControllerTest {
    private static final String REST_URL = ProfileUserController.REST_URL + "/";

    @Autowired
    private UserService service;

    @Test
    void register() throws Exception {
        UserTo newUser = getToFromUser(getNew());
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL + "register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(newUser, "newPassword")))
                .andExpect(status().isCreated());

        User created = readFromJson(action, User.class);
        int id = created.id();
        newUser.setId(id);
        USER_MATCHER.assertMatch(created, getUserFromTo(newUser));
        USER_MATCHER.assertMatch(service.get(id), getUserFromTo(newUser));
    }

    @Test
    void registerInvalid() throws Exception {
        UserTo user = new UserTo(null, "  ", "  ", "  ");
        perform(MockMvcRequestBuilders.post(REST_URL + "register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(user, "password")))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(ErrorType.WRONG_DATA));
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void registerWithDuplicateEmail() throws Exception {
        UserTo user = getToFromUser(getNew());
        user.setEmail(USER_EMAIL);
        perform(MockMvcRequestBuilders.post(REST_URL + "register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(user, "newPassword")))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(ErrorType.WRONG_DATA))
                .andExpect(detailMessage(EXCEPTION_DUPLICATE_EMAIL));
    }

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(user1)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(user1));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void update() throws Exception {
        UserTo user = getToFromUser(getUpdated());
        perform(MockMvcRequestBuilders.put(REST_URL)
                .with(userHttpBasic(user1))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(user, "newPassword")))
                .andDo(print())
                .andExpect(status().isNoContent());

        User updated = getUpdated();
        updated.setRoles(Set.of(Role.USER));
        USER_MATCHER.assertMatch(service.get(FIRST_USER_ID), updated);
    }

    @Test
    void updateInvalid() throws Exception {
        UserTo updated = new UserTo(FIRST_USER_ID, "  ", "  ", "  ");
        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(user1))
                .content(jsonWithPassword(updated, "  ")))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(ErrorType.WRONG_DATA));
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void updateWithDuplicateEmail() throws Exception {
        User updated = new User(user2);
        updated.setEmail(USER_EMAIL);
        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(user2))
                .content(jsonWithPassword(getToFromUser(updated), updated.getPassword())))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(ErrorType.WRONG_DATA))
                .andExpect(detailMessage(EXCEPTION_DUPLICATE_EMAIL));
    }

    @Test
    void updateNotOwn() throws Exception {
        UserTo updated = getToFromUser(getUpdated());
        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(user2))
                .content(jsonWithPassword(updated, updated.getPassword())))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(ErrorType.WRONG_DATA));
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL)
                .with(userHttpBasic(user1)))
                .andDo(print())
                .andExpect(status().isNoContent());

        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(user1)))
                .andExpect(status().isUnauthorized());
    }
}
