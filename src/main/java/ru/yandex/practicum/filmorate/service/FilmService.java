package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FilmService {

    final FilmStorage filmStorage;

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public Film getFilmById(Long id) {
        Film filmById = filmStorage.getFilmById(id);
        if (Objects.isNull(filmById)) {
            String error = String.format("Фильм с id %d не найден", id);
            throw new NotFoundException(HttpStatus.NOT_FOUND, error);
        }
       return filmById;
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public void addLike(Long filmId, Long userId) {
        Film film = getFilmById(filmId);
        film.getLikes().add(userId);
        filmStorage.updateFilm(film);
    }

    public void removeLike(Long filmId, Long userId) {
        Film film = getFilmById(filmId);
        film.getLikes().remove(userId);
        filmStorage.updateFilm(film);
    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage.getAllFilms().stream()
                .sorted(Comparator.comparingInt(film -> -film.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }
}