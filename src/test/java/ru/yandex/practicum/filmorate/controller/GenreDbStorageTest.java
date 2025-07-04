package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreDbStorage;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({GenreDbStorage.class})
class GenreDbStorageTest {
    private final GenreDbStorage genreStorage;

    @Test
    public void testGetAllGenres() {
        List<Genre> genres = genreStorage.getAllGenres();

        assertThat(genres).isNotEmpty();
        assertThat(genres).extracting(Genre::getName).contains("Комедия", "Драма", "Мультфильм");
    }

    @Test
    public void testGetGenreById() {
        Genre genre = genreStorage.getGenreById(1);

        assertThat(genre).isNotNull();
        assertThat(genre.getId()).isEqualTo(1);
        assertThat(genre.getName()).isNotBlank();
    }

    @Test
    public void testGetGenreByIdNotFound() {
        assertThrows(NotFoundException.class, () -> genreStorage.getGenreById(999));
    }

    @Test
    public void testGenreFieldsMapping() {
        Genre genre = genreStorage.getGenreById(1);

        assertThat(genre)
                .hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrProperty("name")
                .hasNoNullFieldsOrProperties();
    }
}