package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import java.util.List;
import java.util.ArrayList;

@RestController
@RequestMapping("/films")
@Validated
public class FilmController {

    private List<Film> films = new ArrayList<>(); // Хранение фильмов в памяти для примера
    private int idCounter = 1; // Счетчик для генерации ID

    @PostMapping
    public Film addFilm(@RequestBody @Valid Film film) {
        film.setId(idCounter++); // Установка ID
        films.add(film); // Сохранение фильма
        return film; // Вернуть добавленный фильм
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Valid Film film) {
        // Логика обновления фильма (поиск и замена)
        for (int i = 0; i < films.size(); i++) {
            if (films.get(i).getId() == film.getId()) {
                films.set(i, film);
                return film; // Вернуть обновленный фильм
            }
        }
        throw new NotFoundException("Фильм не найден");
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return films; // Вернуть список фильмов
    }
}