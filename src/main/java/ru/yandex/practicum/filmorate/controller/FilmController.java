package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
public class FilmController {

    final FilmService filmService;
    final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film addFilm(@RequestBody @Valid Film film) {
        validateFilm(film);
        log.warn("Добавление фильма");
        return filmService.addFilm(film);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Film updateFilm(@RequestBody @Valid Film film) {
        validateFilm(film);
        log.warn("Обновление фильма");
        return filmService.updateFilm(film);
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable Long id) {
        log.warn("Получение фильма по id: {}", id);
        return filmService.getFilmById(id);
    }

    @GetMapping
    public List<Film> getAllFilms() {
        log.warn("Получение всех фильмов");
        return filmService.getAllFilms();
    }

    @PutMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void addLike(@PathVariable Long id, @PathVariable Long userId) {
        userService.getUserById(userId);
        log.warn("Пользователь с id {} ставит лайк фильму с id {}", userId, id);
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void removeLike(@PathVariable Long id, @PathVariable Long userId) {
        userService.getUserById(userId);
        log.warn("Пользователь с id {} удаляет лайк у фильма с id {}", userId, id);
        filmService.removeLike(id, userId);
    }

    @GetMapping("/popular")
    @ResponseStatus(HttpStatus.OK)
    public List<Film> getPopularFilms(@RequestParam(required = false, defaultValue = "10") int count) {
        log.warn("Получение популярных фильмов, количество: {}", count);
        return filmService.getPopularFilms(count);
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
}