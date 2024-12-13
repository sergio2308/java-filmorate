package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private int id = 1;
    private Map<Integer, Film> filmsMap = new HashMap<>();

    @ResponseStatus
    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        validateFilm(film);
        int id = nextId();
        film.setId(id);
        filmsMap.put(film.getId(), film);
        // Логика добавления фильма
        return film; // Вернуть добавленный фильм
    }

    private void validateFilm(Film film) {
        if (film.getName() == null || film.getName().isEmpty()) {
            throw new ValidationException("Название фильма не может быть пустым");
        }
        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата выхода недействительна");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность должна быть положительной");
        }
    }

    @ResponseStatus
    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        validateFilm(film);
        // Логика обновления фильма
        return film; // Вернуть обновленный фильм
    }

    @GetMapping
    @RequestMapping("/{id}")
    public ResponseEntity<Film> getFilmById(@PathVariable int id) {
        if (filmsMap.containsKey(id)) {
            return ResponseEntity.ok().body(filmsMap.get(id));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public List<Film> getAllFilms() {
        // Логика получения всех фильмов
        return List.of(); // Вернуть список фильмов
    }

    private int nextId() {
        return id++;
    }
}