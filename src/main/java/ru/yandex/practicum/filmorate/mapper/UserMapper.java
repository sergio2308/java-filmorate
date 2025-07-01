package ru.yandex.practicum.filmorate.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

@Component
public class UserMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        Long userId = rs.getLong("user_id");
        String email = rs.getString("email");
        String login = rs.getString("login");
        String name = rs.getString("name");
        LocalDate birthday = rs.getDate("birthday").toLocalDate();

        return User.builder()
                .id(userId)
                .email(email)
                .login(login)
                .name(name)
                .birthday(birthday)
                .build();
    }
}