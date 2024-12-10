package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@Validated
public class UserController {

    @PostMapping
    public User createUser (@RequestBody @Valid User user) {
        // Логика создания пользователя
        return user; // Вернуть созданного пользователя
    }

    @PutMapping
    public User updateUser (@RequestBody @Valid User user) {
        // Логика обновления пользователя
        return user; // Вернуть обновленного пользователя
    }

    @GetMapping
    public List<User> getAllUsers() {
        // Логика получения всех пользователей
        return List.of(); // Вернуть список пользователей
    }
}