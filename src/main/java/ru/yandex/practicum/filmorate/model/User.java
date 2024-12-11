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

    @NotBlank(message = "Login cannot be blank")
    private String login;

    private String name;

    @Email(message = "Email should be valid")
    private String email;

    @Past(message = "Birthday must be in the past")
    private LocalDate birthday;
}