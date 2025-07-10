package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class UserControllerTests {

    private UserController userController;
    private UserService userService;

    @BeforeEach
    public void setUp() {
        JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);
        UserDbStorage userDbStorage = mock(UserDbStorage.class);
        InMemoryUserStorage userStorage = new InMemoryUserStorage();

        userService = new UserService(jdbcTemplate, userStorage);
        userController = new UserController(userService);
    }

    @Test
    public void testCreateUserSuccessfully() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("testlogin");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        User createdUser = userController.createUser(user);

        assertNotNull(createdUser);
        assertEquals(1, createdUser.getId());
        assertEquals("testlogin", createdUser.getName());
        assertEquals("test@example.com", createdUser.getEmail());
    }

    @Test
    public void testCreateUserWithEmptyName() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("testlogin");
        user.setBirthday(LocalDate.of(1990, 1, 1));
        user.setName(null);

        User createdUser = userController.createUser(user);

        assertEquals("testlogin", createdUser.getName());
    }

    @Test
    public void testCreateUserWithInvalidEmail() {
        User user = new User();
        user.setEmail("invalid-email");
        user.setLogin("testlogin");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        Exception exception = assertThrows(ValidationException.class, () -> userController.createUser(user));
        assertEquals("Недопустимый email", exception.getMessage());
    }

    @Test
    public void testCreateUserWithEmptyLogin() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin(" ");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        Exception exception = assertThrows(ValidationException.class, () -> userController.createUser(user));
        assertEquals("Логин не может быть пустым", exception.getMessage());
    }

    @Test
    public void testCreateUserWithFutureBirthday() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("testlogin");
        user.setBirthday(LocalDate.now().plusDays(1));

        Exception exception = assertThrows(ValidationException.class, () -> userController.createUser(user));
        assertEquals("Дата рождения не может быть в будущем", exception.getMessage());
    }

    @Test
    public void testUpdateUserSuccessfully() {
        User initialUser = new User();
        initialUser.setEmail("test@example.com");
        initialUser.setLogin("testlogin");
        initialUser.setBirthday(LocalDate.of(1990, 1, 1));
        User createdUser = userController.createUser(initialUser);

        User updatedUser = new User();
        updatedUser.setId(createdUser.getId());
        updatedUser.setEmail("updated@example.com");
        updatedUser.setLogin("updatedlogin");
        updatedUser.setBirthday(LocalDate.of(1995, 5, 5));

        User result = userController.updateUser(updatedUser);

        assertNotNull(result);
        assertEquals(updatedUser.getId(), result.getId());
        assertEquals("updated@example.com", result.getEmail());
        assertEquals("updatedlogin", result.getLogin());
    }

    @Test
    public void testUpdateNonexistentUser() {
        User user = new User();
        user.setId(999L);
        user.setEmail("nonexistent@example.com");
        user.setLogin("nonexistentlogin");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        Exception exception = assertThrows(NotFoundException.class, () -> userController.updateUser(user));
        assertEquals("Пользователь не найден с ID: 999", exception.getMessage());
    }

    @Test
    public void testGetAllUsersInitiallyEmpty() {
        List<User> usersList = userController.getAllUsers();
        assertTrue(usersList.isEmpty());
    }

    @Test
    public void testGetAllUsersWithUsers() {
        User user1 = new User();
        user1.setEmail("test1@example.com");
        user1.setLogin("login1");
        user1.setBirthday(LocalDate.of(1990, 1, 1));

        User user2 = new User();
        user2.setEmail("test2@example.com");
        user2.setLogin("login2");
        user2.setBirthday(LocalDate.of(1995, 5, 5));

        userController.createUser(user1);
        userController.createUser(user2);

        List<User> usersList = userController.getAllUsers();
        assertEquals(2, usersList.size());
    }
}