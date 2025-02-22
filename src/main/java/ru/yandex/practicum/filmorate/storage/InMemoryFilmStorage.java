package ru.yandex.practicum.filmorate.storage;

import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private Map<Long, Film> filmsMap = new HashMap<Long, Film>();
    private Long id = 1L;
    
    @Override
    public Film addFilm(Film film) {
        @NotNull Long id = nextId();
        film.setId(id);
        filmsMap.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (!filmsMap.containsKey(film.getId())) {
            throw new NotFoundException(HttpStatus.NOT_FOUND, "Фильм не найден с ID: " + film.getId());
        }
        filmsMap.put(film.getId(), film);
        return film;
    }

    @Override
    public Film getFilmById(Long id) {
        return filmsMap.get(id);
    }

    @Override
    public List<Film> getAllFilms() {
        return List.copyOf(filmsMap.values()); // Вернуть список всех фильмов
    }
    
    private @NotNull Long nextId() {
        return id++;
    }
}