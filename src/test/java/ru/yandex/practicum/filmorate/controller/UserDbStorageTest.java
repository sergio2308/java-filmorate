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
        User newUser = User.builder()
                .email("user@email.ru")
                .login("user")
                .name("User Name")
                .birthday(Timestamp.valueOf(LocalDateTime.now().minusYears(20)))
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
        User user = User.builder()
                .email("user@email.ru")
                .login("user")
                .name("User Name")
                .birthday(Timestamp.valueOf(LocalDateTime.now().minusYears(20)))
                .build();

        userStorage.createUser(user);

        user.setName("Updated Name");
        userStorage.updateUser(user);

        User updatedUser = userStorage.getUserById(user.getId());

        assertThat(updatedUser.getName()).isEqualTo("Updated Name");
    }
}