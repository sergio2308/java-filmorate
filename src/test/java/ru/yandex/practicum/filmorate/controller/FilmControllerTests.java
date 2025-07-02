package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.MpaService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.*;

import static org.mockito.Mockito.mock;

import java.time.LocalDate;
import java.util.List;

public class FilmControllerTests {

    private FilmController filmController;
    private FilmService filmService;
    private UserService userService;
    private MpaService mpaService;
    private GenreService genreService;


    @BeforeEach
    public void setUp() {
        JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);
        FilmDbStorage filmDbStorage = mock(FilmDbStorage.class);
        FilmStorage filmStorage = new InMemoryFilmStorage();

        filmService = new FilmService(jdbcTemplate, filmDbStorage, filmStorage, mpaService, genreService);

        JdbcTemplate jdbcTemplateUser = mock(JdbcTemplate.class);
        UserDbStorage userDbStorage = mock(UserDbStorage.class);
        UserStorage userStorage = new InMemoryUserStorage();

        userService = new UserService(jdbcTemplateUser, userDbStorage, userStorage);

        filmController = new FilmController(filmService, userService, genreService, mpaService);
    }


    @Test
    public void testAddFilmSuccessfully() {
        Film film = new Film();
        film.setName("Film Name");
        film.setDescription("This is a valid description.");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);
        film.setMpa(new Mpa(1, "G")); // Добавляем обязательный MPA

        Film addedFilm = filmController.addFilm(film);

        Assertions.assertNotNull(addedFilm);
        Assertions.assertEquals(1, addedFilm.getId());
        Assertions.assertEquals("Film Name", addedFilm.getName());
    }

    @Test
    public void testAddFilmWithInvalidName() {
        Film film = new Film();
        film.setName("");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);

        Exception exception = Assertions.assertThrows(ValidationException.class, () -> {
            filmController.addFilm(film);
        });
        Assertions.assertEquals("Название фильма не может быть пустым", exception.getMessage());
    }

    @Test
    public void testAddFilmWithDescriptionTooLong() {
        Film film = new Film();
        film.setName("Name");
        film.setDescription("D".repeat(201));
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(100);

        Assertions.assertThrows(ValidationException.class, () -> {
            filmController.addFilm(film);
        });
    }

    @Test
    public void testAddFilmWithInvalidReleaseDate() {
        Film film = new Film();
        film.setName("Name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(1800, 1, 1));
        film.setDuration(100);

        Assertions.assertThrows(ValidationException.class, () -> {
            filmController.addFilm(film);
        });
    }

    @Test
    public void testAddFilmWithInvalidDuration() {
        Film film = new Film();
        film.setName("Name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(-100);

        Assertions.assertThrows(ValidationException.class, () -> {
            filmController.addFilm(film);
        });
    }

    @Test
    public void testUpdateFilmSuccessfully() {
        Film film = new Film();
        film.setName("Film to Update");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(100);
        film.setMpa(new Mpa(1, "G")); // Добавляем обязательный MPA

        Film addedFilm = filmController.addFilm(film);
        addedFilm.setName("Updated Film Name");
        Film updatedFilm = filmController.updateFilm(addedFilm);

        Assertions.assertEquals("Updated Film Name", updatedFilm.getName());
    }

    @Test
    public void testUpdateNonExistentFilm() {
        Film film = new Film();
        film.setId(999L);
        film.setName("Non-existent film");
        film.setDescription("Valid description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);
        film.setMpa(new Mpa(1, "G"));

        Exception exception = Assertions.assertThrows(NotFoundException.class, () -> {
            filmController.updateFilm(film);
        });
        Assertions.assertTrue(exception.getMessage().contains("не найден"));
    }

    @Test
    public void testGetFilmByIdSuccessfully() {
        Film film = new Film();
        film.setName("New Film");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);

        Film addedFilm = filmController.addFilm(film);

        Assertions.assertEquals(addedFilm, filmController.getFilmById(addedFilm.getId()));
    }

    @Test
    public void testGetNonExistentFilmById() {
        Assertions.assertThrows(NotFoundException.class, () -> {
            filmController.getFilmById(999L);
        });
    }

    @Test
    public void testGetAllFilms() {
        Film film1 = new Film();
        film1.setName("Film 1");
        film1.setDescription("Description");
        film1.setReleaseDate(LocalDate.of(2000, 1, 1));
        film1.setDuration(100);

        Film film2 = new Film();
        film2.setName("Film 2");
        film2.setDescription("Another Description");
        film2.setReleaseDate(LocalDate.of(2002, 5, 20));
        film2.setDuration(150);

        filmController.addFilm(film1);
        filmController.addFilm(film2);

        List<Film> allFilms = filmController.getAllFilms();

        Assertions.assertEquals(2, allFilms.size());
    }
}