package ru.yandex.practicum.filmorate.storage;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {

    private Long id = 1L;
    private Map<Long, User> usersMap = new HashMap<>();

    @Override
    public User createUser(User user) {
        Long id = nextId();
        user.setId(id);
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        usersMap.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (!usersMap.containsKey(user.getId())) {
            throw new NotFoundException(HttpStatus.NOT_FOUND, "Пользователь не найден с ID: " + user.getId());
        }
        usersMap.put(user.getId(), user);
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return List.copyOf(usersMap.values());
    }

    @Override
    public User getUserById(Long id) {
        return usersMap.get(id);
    }

    private Long nextId() {
        return id++;
    }
}