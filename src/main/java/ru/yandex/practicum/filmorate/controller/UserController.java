package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import java.util.List;
import java.util.ArrayList;

@RestController
@RequestMapping("/users")
@Validated
public class UserController {

    private List<User> users = new ArrayList<>(); // Хранение пользователей в памяти для примера
    private int idCounter = 1; // Счетчик для генерации ID

    @PostMapping
    public User createUser(@RequestBody @Valid User user) {
        user.setId(idCounter++); // Установка ID
        users.add(user); // Сохранение пользователя
        return user; // Вернуть созданного пользователя
    }

    @PutMapping
    public User updateUser(@RequestBody @Valid User user) {
        // Логика обновления пользователя (поиск и замена)
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId() == user.getId()) {
                users.set(i, user);
                return user; // Вернуть обновленного пользователя
            }
        }
        throw new NotFoundException("Пользователь не найден");
    }

    @GetMapping
    public List<User> getAllUsers() {
        return users; // Вернуть список пользователей
    }
}