package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    @PostMapping
    public User createUser(@RequestBody User user) {
        validateUser(user);
        // Логика создания пользователя
        return user; // Вернуть созданного пользователя
    }

    private void validateUser(User user) {
        if (user.getLogin() == null || user.getLogin().isEmpty()) {
            throw new ValidationException("Логин не может быть пустым");
        }
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            throw new ValidationException("Недопустимый email");
        }
        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        validateUser(user);
        // Логика обновления пользователя
        return user; // Вернуть обновленного пользователя
    }

    @GetMapping
    public List<User> getAllUsers() {
        // Логика получения всех пользователей
        return List.of(); // Вернуть список пользователей
    }
}