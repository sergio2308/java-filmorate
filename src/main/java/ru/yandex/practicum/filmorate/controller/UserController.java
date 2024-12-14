package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private int id = 1;
    private Map<Integer, User> usersMap = new HashMap<>();

    @PostMapping
    public User createUser(@RequestBody @Valid User user) {
        validateUser(user);
        int id = nextId();
        user.setId(id);
        if (user.getName() == null) {
            user.setName(user.getLogin()); // Значение name равно login, если name не задан
        }
        usersMap.put(user.getId(), user);
        return user;
        // Логика создания пользователя
        // Вернуть созданного пользователя
    }

    private void validateUser(User user) {
        if (user.getLogin() == null || user.getLogin().isEmpty() || user.getLogin().isBlank()) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Логин не может быть пустым");
        }
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Недопустимый email");
        }
        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Дата рождения не может быть в будущем");
        }
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        if (!usersMap.containsKey(user.getId())) {
            throw new NotFoundException(HttpStatus.NOT_FOUND, "Фильм не найден с ID: " + user.getId());
        }
        validateUser(user);
        // Логика обновления пользователя
        return user; // Вернуть обновленного пользователя
    }

    @GetMapping
    public List<User> getAllUsers() {
        // Логика получения всех пользователей
        return List.copyOf(usersMap.values()); // Вернуть список пользователей
    }

    private int nextId() {
        return id++;
    }
}