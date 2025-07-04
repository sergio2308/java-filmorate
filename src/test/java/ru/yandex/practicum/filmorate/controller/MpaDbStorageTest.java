package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaDbStorage;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({MpaDbStorage.class})
class MpaDbStorageTest {
    private final MpaDbStorage mpaStorage;

    @Test
    public void testGetAllMpa() {
        List<Mpa> mpaList = mpaStorage.getAllMpa();
        assertThat(mpaList).isNotEmpty();
        assertThat(mpaList).extracting(Mpa::getName).contains("G", "PG", "PG-13");
    }

    @Test
    public void testGetMpaById() {
        Mpa mpa = mpaStorage.getMpaById(1);
        assertThat(mpa).isNotNull();
        assertThat(mpa.getId()).isEqualTo(1);
        assertThat(mpa.getName()).isNotBlank();
    }

    @Test
    public void testGetMpaByIdNotFound() {
        assertThrows(NotFoundException.class, () -> mpaStorage.getMpaById(999));
    }

    @Test
    public void testMpaFieldsMapping() {
        Mpa mpa = mpaStorage.getMpaById(1);
        assertThat(mpa)
                .hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrProperty("name")
                .hasNoNullFieldsOrProperties();
    }
}