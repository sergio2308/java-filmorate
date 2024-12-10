package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        // Логика добавления фильма
        return film; // Вернуть добавленный фильм
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        // Логика обновления фильма
        return film; // Вернуть обновленный фильм
    }

    @GetMapping
    public List<Film> getAllFilms() {
        // Логика получения всех фильмов
        return List.of(); // Вернуть список фильмов
    }
}