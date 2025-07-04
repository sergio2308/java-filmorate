package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.sql.*;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@Component
@Primary
@Qualifier("dbStorage")
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final MpaStorage mpaStorage;
    private final GenreService genreStorage;

    @Override
    public Film addFilm(Film film) {
        validateGenresExist(film.getGenres());
        validateMpaExist(film.getMpa());
        String sql = "INSERT INTO Films (name, description, release_date, duration, mpa_id) VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);

        Long filmId = Objects.requireNonNull(keyHolder.getKey()).longValue();
        film.setId(filmId);
        saveFilmGenres(film);
        saveFilmLikes(film);

        return getFilmById(filmId);
    }

    @Override
    public Film updateFilm(Film film) {
        validateGenresExist(film.getGenres());
        validateMpaExist(film.getMpa());
        String sql = "UPDATE Films SET name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ? WHERE film_id = ?";
        int updated = jdbcTemplate.update(sql, film.getName(), film.getDescription(), Date.valueOf(film.getReleaseDate()), film.getDuration(), film.getMpa().getId(), film.getId());

        if (updated == 0) {
            throw new NotFoundException("Фильм с id " + film.getId() + " не найден");
        }

        // Обновляем жанры
        String deleteGenresSql = "DELETE FROM Film_genre WHERE film_id = ?";
        jdbcTemplate.update(deleteGenresSql, film.getId());
        saveFilmGenres(film);

        // Обновляем лайки
        String deleteLikesSql = "DELETE FROM Film_likes WHERE film_id = ?";
        jdbcTemplate.update(deleteLikesSql, film.getId());
        saveFilmLikes(film);

        return getFilmById(film.getId());
    }

    private void saveFilmGenres(Film film) {
        if (film.getGenres() == null || film.getGenres().isEmpty()) {
            return;
        }
        String sql = "MERGE INTO Film_genre (film_id, genre_id) KEY (film_id, genre_id) VALUES (?, ?)";
        for (Genre genre : film.getGenres()) {
            jdbcTemplate.update(sql, film.getId(), genre.getId());
        }
    }

    private void saveFilmLikes(Film film) {
        if (film.getLikes() == null || film.getLikes().isEmpty()) {
            return;
        }
        String sql = "MERGE INTO Film_likes (film_id, user_id) KEY (film_id, user_id) VALUES (?, ?)";
        for (Long userId : film.getLikes()) {
            jdbcTemplate.update(sql, film.getId(), userId);
        }
    }

    @Override
    public Film getFilmById(Long id) {
        String sqlFilm = "SELECT * FROM Films WHERE film_id = ?";
        List<Film> films = jdbcTemplate.query(sqlFilm, (rs, rowNum) -> mapRowToFilm(rs), id);
        if (films.isEmpty()) {
            throw new NotFoundException("Фильм с id " + id + " не найден");
        }

        Film film = films.get(0);

        // Загружаем жанры
        String sqlGenres = "SELECT g.genre_id, g.name FROM Genres g " + "JOIN Film_genre fg ON g.genre_id = fg.genre_id " + "WHERE fg.film_id = ?";
        List<Genre> genres = jdbcTemplate.query(sqlGenres, (rs, rowNum) -> new Genre(rs.getInt("genre_id"), rs.getString("name")), id);
        film.setGenres(genres);

        // Загружаем рейтинг MPA с обработкой отсутствия
        if (film.getMpa() == null || film.getMpa().getId() == null) {
            throw new NotFoundException("Рейтинг MPA не задан для фильма с id " + id);
        }
        String sqlMpa = "SELECT mpa_id, name FROM Mpa WHERE mpa_id = ?";
        Mpa mpa;
        try {
            mpa = jdbcTemplate.queryForObject(sqlMpa, (rs, rowNum) -> new Mpa(rs.getInt("mpa_id"), rs.getString("name")), film.getMpa().getId());
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Рейтинг MPA с id " + film.getMpa().getId() + " не найден");
        }
        film.setMpa(mpa);

        // Загружаем лайки
        String sqlLikes = "SELECT user_id FROM Film_likes WHERE film_id = ?";
        List<Long> likes = jdbcTemplate.query(sqlLikes, (rs, rowNum) -> rs.getLong("user_id"), id);
        film.setLikes(new HashSet<>(likes));

        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        String sql = "SELECT * FROM Films";
        List<Film> films = jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToFilm(rs));

        for (Film film : films) {
            // Загружаем жанры
            String sqlGenres = "SELECT g.genre_id, g.name FROM Genres g " + "JOIN Film_genre fg ON g.genre_id = fg.genre_id " + "WHERE fg.film_id = ?";
            List<Genre> genres = jdbcTemplate.query(sqlGenres, (rs, rowNum) -> new Genre(rs.getInt("genre_id"), rs.getString("name")), film.getId());
            film.setGenres(genres);

            // Загружаем рейтинг
            String sqlMpa = "SELECT mpa_id, name FROM Mpa WHERE mpa_id = ?";
            Mpa mpa = jdbcTemplate.queryForObject(sqlMpa, (rs, rowNum) -> new Mpa(rs.getInt("mpa_id"), rs.getString("name")), film.getMpa().getId());
            film.setMpa(mpa);

            // Загружаем лайки
            String sqlLikes = "SELECT user_id FROM Film_likes WHERE film_id = ?";
            List<Long> likes = jdbcTemplate.query(sqlLikes, (rs, rowNum) -> rs.getLong("user_id"), film.getId());
            film.setLikes(new HashSet<>(likes));
        }

        return films;
    }

    private Film mapRowToFilm(ResultSet rs) throws SQLException {
        Film film = new Film();
        film.setId(rs.getLong("film_id"));
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        film.setReleaseDate(rs.getDate("release_date").toLocalDate());
        film.setDuration(rs.getInt("duration"));

        int mpaId = rs.getInt("mpa_id");
        if (rs.wasNull() || mpaId == 0) {
            film.setMpa(null);
        } else {
            film.setMpa(new Mpa(mpaId, null));
        }

        film.setLikes(new HashSet<>());
        return film;
    }

    private void validateGenresExist(List<Genre> genres) {
        if (genres == null) {
            return;
        }
        for (Genre genre : genres) {
            try {
                genreStorage.getGenreById(genre.getId());
            } catch (NotFoundException e) {
                throw new NotFoundException("Жанр с id " + genre.getId() + " не найден");
            }
        }
    }

    private void validateMpaExist(Mpa mpa) {
        if (mpa == null || mpa.getId() == null) {
            throw new ValidationException("Рейтинг не может быть пустым");
        }
        try {
            mpaStorage.getMpaById(mpa.getId());
        } catch (NotFoundException e) {
            throw new NotFoundException("Рейтинг с id " + mpa.getId() + " не найден");
        }
    }
}