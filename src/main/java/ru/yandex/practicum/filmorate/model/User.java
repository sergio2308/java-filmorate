package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class User {
    private int id;

    @NotBlank(message = "Логин не может быть пустым")
    private String login;

    private String name;

    @Email(message = "Email должен быть корректным")
    private String email;

    @Past(message = "Дата рождения должна быть в прошлом")
    private LocalDate birthday;

    // Конструктор для автоматической установки имени по умолчанию
    public User(String login, String email, LocalDate birthday) {
        this.login = login;
        this.email = email;
        this.birthday = birthday;
        this.name = login; // Устанавливаем имя по умолчанию как логин
    }
}