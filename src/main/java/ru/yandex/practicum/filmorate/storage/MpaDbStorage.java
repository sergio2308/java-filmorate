package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Mpa> getAllMpa() {
        String sql = "SELECT * FROM Mpa";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToMpa(rs));
    }

    @Override
    public Mpa getMpaById(Integer id) {
        String sql = "SELECT * FROM Mpa WHERE mpa_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new Mpa(rs.getInt("mpa_id"), rs.getString("name")), id);
        } catch (Exception e) {
            throw new NotFoundException("Рейтинг с id " + id + " не найден");
        }
    }

    private Mpa mapRowToMpa(ResultSet rs) throws SQLException {
        return Mpa.builder().id(rs.getInt("mpa_id")).name(rs.getString("name")).build();
    }
}