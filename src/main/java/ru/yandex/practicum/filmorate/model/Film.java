package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
public class Film {
    private int id;

    @NotBlank(message = "Название фильма не должно быть пустым")
    private String name;

    @NotBlank(message = "Описание фильма не должно быть пустым")
    private String description;

    @Past(message = "Дата выпуска должна быть в прошлом")
    private LocalDate releaseDate;

    private int duration; // продолжительность в минутах
}