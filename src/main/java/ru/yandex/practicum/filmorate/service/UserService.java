package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class UserService {
    private JdbcTemplate jdbcTemplate;

    final UserStorage userStorage;

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User getUserById(Long id) {
        User userById = userStorage.getUserById(id);
        if (Objects.isNull(userById)) {
            String error = String.format("Пльзователь с id %d не найден", id);
            throw new NotFoundException(error);
        }
        return userStorage.getUserById(id);
    }

    public void addFriend(Long userId, Long friendId) {
        String sql = "INSERT INTO Friends (user_id, friend_id, status) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, userId, friendId, false);
    }

    public void removeFriend(Long userId, Long friendId) {
        getUserById(userId);
        getUserById(friendId);
        String sql = "DELETE FROM Friends WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sql, userId, friendId);
    }

    public List<User> getFriends(Long userId) {
        getUserById(userId); // проверка существования пользователя
        String sql = "SELECT u.* FROM Users u " + "JOIN Friends f ON u.user_id = f.friend_id " + "WHERE f.user_id = ?";
        return jdbcTemplate.query(sql, new UserMapper(), userId);
    }

    public List<User> getCommonFriends(Long userId, Long otherId) {
        String sql = "SELECT u.* FROM Users u " + "JOIN Friends f1 ON u.user_id = f1.friend_id " + "JOIN Friends f2 ON u.user_id = f2.friend_id " + "WHERE f1.user_id = ? AND f2.user_id = ?";
        return jdbcTemplate.query(sql, new UserMapper(), userId, otherId);
    }
}