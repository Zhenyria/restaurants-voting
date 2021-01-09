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
import ru.zhenyria.restaurants.util.exception.ErrorType;
import ru.zhenyria.restaurants.util.exception.NotFoundException;
import ru.zhenyria.restaurants.web.AbstractControllerTest;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.zhenyria.restaurants.TestUtil.readFromJson;
import static ru.zhenyria.restaurants.TestUtil.userHttpBasic;
import static ru.zhenyria.restaurants.UserTestData.*;
import static ru.zhenyria.restaurants.web.ExceptionInfoHandler.EXCEPTION_DUPLICATE_EMAIL;

class AdminUserControllerTest extends AbstractControllerTest {
    private static final String REST_URL = AdminUserController.REST_URL + "/";

    @Autowired
    private UserService service;

    @Test
    void createWithLocation() throws Exception {
        User newUser = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin))
                .content(jsonWithPassword(newUser, "password")))
                .andExpect(status().isCreated());

        User created = readFromJson(action, User.class);
        int id = created.id();
        newUser.setId(id);
        USER_MATCHER.assertMatch(created, newUser);
        USER_MATCHER.assertMatch(service.get(id), newUser);
    }

    @Test
    void createInvalid() throws Exception {
        User newUser = new User("  ", "  ", "  ", Role.USER);
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin))
                .content(jsonWithPassword(newUser, "password")))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(ErrorType.WRONG_DATA));
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void createWithDuplicateEmail() throws Exception {
        User newUser = getNew();
        newUser.setEmail(USER_EMAIL);
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin))
                .content(jsonWithPassword(newUser, "newPassword")))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(ErrorType.WRONG_DATA))
                .andExpect(detailMessage(EXCEPTION_DUPLICATE_EMAIL));
    }

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + FIRST_USER_ID)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(user1));
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + NOT_FOUND_ID)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(errorType(ErrorType.NOT_FOUND));
    }

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(users));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(user1)))
                .andExpect(status().isForbidden());
    }

    @Test
    void update() throws Exception {
        User updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL)
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(updated, updated.getPassword())))
                .andDo(print())
                .andExpect(status().isNoContent());

        USER_MATCHER.assertMatch(service.get(FIRST_USER_ID), getUpdated());
    }

    @Test
    void updateInvalid() throws Exception {
        User updated = new User(FIRST_USER_ID, "  ", "  ", "  ", Role.USER);
        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin))
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
                .with(userHttpBasic(admin))
                .content(jsonWithPassword(updated, updated.getPassword())))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(ErrorType.WRONG_DATA))
                .andExpect(detailMessage(EXCEPTION_DUPLICATE_EMAIL));
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + FIRST_USER_ID)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> service.get(FIRST_USER_ID));
    }

    @Test
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + NOT_FOUND_ID)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(errorType(ErrorType.NOT_FOUND));
    }
}