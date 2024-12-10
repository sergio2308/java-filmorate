package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @PostMapping
    public User createUser (@RequestBody User user) {
        // Логика создания пользователя
        return user; // Вернуть созданного пользователя
    }

    @PutMapping
    public User updateUser (@RequestBody User user) {
        // Логика обновления пользователя
        return user; // Вернуть обновленного пользователя
    }

    @GetMapping
    public List<User> getAllUsers() {
        // Логика получения всех пользователей
        return List.of(); // Вернуть список пользователей
    }
}