package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.exception.NotFoundException; // Импортируем NotFoundException
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {

    private int id = 1;
    private Map<Integer, Film> filmsMap = new HashMap<>();

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Film addFilm(@RequestBody @Valid Film film) {
        validateFilm(film); // Валидация фильма
        int id = nextId();
        film.setId(id);
        filmsMap.put(film.getId(), film);
        return film; // Вернуть добавленный фильм
    }

    private void validateFilm(Film film) {
        if (film.getName() == null || film.getName().isEmpty() || film.getName().isBlank()) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Название фильма не может быть пустым");
        }
        if (film.getDescription() == null || film.getDescription().length() > 200 || film.getDescription().isEmpty()
            || film.getDescription().isBlank()) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Описание не должно превышать 200 символов");
        }
        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Дата выхода недействительна");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Продолжительность должна быть положительной");
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        if (!filmsMap.containsKey(film.getId())) {
            throw new NotFoundException(HttpStatus.NOT_FOUND, "Фильм не найден с ID: " + film.getId());
        }
        validateFilm(film); // Валидация фильма
        filmsMap.put(film.getId(), film); // Обновляем фильм
        return film; // Вернуть обновленный фильм
    }

    @GetMapping("/{id}")
    public ResponseEntity<Film> getFilmById(@PathVariable int id) {
        if (filmsMap.containsKey(id)) {
            return ResponseEntity.ok().body(filmsMap.get(id));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return List.copyOf(filmsMap.values()); // Вернуть список всех фильмов
    }

    private int nextId() {
        return id++;
    }
}