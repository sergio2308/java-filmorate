package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserDbStorage.class})
class UserDbStorageTest {
    private final UserDbStorage userStorage;

    @Test
    public void testFindUserById() {
        User.UserBuilder builder = User.builder();
        builder.email("user@email.ru");
        builder.login("user");
        builder.name("User Name");
        builder.birthday(LocalDate.now().minusYears(20));
        User newUser = builder
                .build();

        userStorage.createUser(newUser);

        User savedUser = userStorage.getUserById(newUser.getId());

        assertThat(savedUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newUser);
    }

    @Test
    public void testUpdateUser() {
        User.UserBuilder builder = User.builder();
        builder.email("user@email.ru");
        builder.login("user");
        builder.name("User Name");
        builder.birthday(LocalDate.now().minusYears(20));
        User user = builder
                .build();

        userStorage.createUser(user);

        user.setName("Updated Name");
        userStorage.updateUser(user);

        User updatedUser = userStorage.getUserById(user.getId());

        assertThat(updatedUser.getName()).isEqualTo("Updated Name");
    }
}