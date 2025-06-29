package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Rating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RatingDbStorage implements RatingStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Rating> getAllRatings() {
        String sql = "SELECT * FROM Rating";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToRating(rs));
    }

    @Override
    public Rating getRatingById(Integer id) {
        String sql = "SELECT * FROM Rating WHERE rating_id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> mapRowToRating(rs), id);
    }

    private Rating mapRowToRating(ResultSet rs) throws SQLException {
        return Rating.builder()
                .id(rs.getInt("rating_id"))
                .name(rs.getString("name"))
                .build();
    }
}