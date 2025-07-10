package ru.yandex.practicum.filmorate.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Component
public class FriendshipMapper implements RowMapper<Map<Long, Boolean>> {
    @Override
    public Map<Long, Boolean> mapRow(ResultSet rs, int rowNum) throws SQLException {
        Map<Long, Boolean> friends = new HashMap<>();
        do {
            friends.put(rs.getLong("friend_id"), rs.getBoolean("status"));
        } while (rs.next());
        return friends;
    }
}