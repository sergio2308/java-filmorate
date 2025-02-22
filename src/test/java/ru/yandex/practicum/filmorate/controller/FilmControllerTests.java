package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;

public class FilmControllerTests {

    private FilmController filmController;

    @BeforeEach
    public void setUp() {
    }

    @Test public void testAddFilmSuccessfully() {
        Film film = new Film();
        film.setName("Film Name");
        film.setDescription("This is a valid description.");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);

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

        Assertions.assertThrows(ValidationException.class, () -> {
            filmController.addFilm(film);
        });
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

        Film addedFilm = filmController.addFilm(film);

        addedFilm.setName("Updated Film Name");
        Film updatedFilm = filmController.updateFilm(addedFilm);

        Assertions.assertEquals("Updated Film Name", updatedFilm.getName());
    }

    @Test
    public void testUpdateNonExistentFilm() {
        Film film = new Film();
        film.setId(100L);
        film.setName("Non-existent film");

        Assertions.assertThrows(NotFoundException.class, () -> {
            filmController.updateFilm(film);
        });
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