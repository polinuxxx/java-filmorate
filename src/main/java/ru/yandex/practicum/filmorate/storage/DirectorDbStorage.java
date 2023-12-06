package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.model.Director;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DAO для {@link Director}.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DirectorDbStorage implements DirectorStorage {

    private final JdbcTemplate jdbcTemplate;

    static Director toDirector(ResultSet rs, int rowNum) throws SQLException {
        return Director.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public Director getById(Long id) {
        String sql = "SELECT id, name FROM directors WHERE id = ?";

        return jdbcTemplate.queryForObject(sql, DirectorDbStorage::toDirector, id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Director> getAll() {
        String sql = "select id, name from directors order by id";

        return jdbcTemplate.query(sql, DirectorDbStorage::toDirector);
    }

    @Override
    @Transactional
    public Director create(Director director) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("directors")
                .usingGeneratedKeyColumns("id");

        director.setId(simpleJdbcInsert.executeAndReturnKey(toMap(director)).longValue());

        return getById(director.getId());
    }

    @Override
    @Transactional
    public Director update(Director director) {
        String sql = "UPDATE directors SET name = ? WHERE id = ?";

        jdbcTemplate.update(sql,
                director.getName(),
                director.getId());

        return getById(director.getId());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        String sql = "DELETE FROM directors WHERE id = ?";

        jdbcTemplate.update(sql, id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(Long id) {
        String sql = "select exists(select id from directors where id = ?)";

        Boolean exists = jdbcTemplate.queryForObject(sql, Boolean.class, id);

        return exists != null && exists;
    }

    private Map<String, Object> toMap(Director director) {
        Map<String, Object> values = new HashMap<>();
        values.put("name", director.getName());

        return values;
    }
}
