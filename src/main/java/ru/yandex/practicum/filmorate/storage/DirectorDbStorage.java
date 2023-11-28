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
import java.util.List;

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

        List<Director> directors = jdbcTemplate.query(sql, DirectorDbStorage::toDirector, id);

        return directors.get(0);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Director> getAll() {
        String sql = "select id, name from directors order by id";

        return jdbcTemplate.query(sql, DirectorDbStorage::toDirector);
    }

    @Override
    @Transactional(readOnly = true)
    public Director create(Director director) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("directors")
                .usingGeneratedKeyColumns("id");

        director.setId(simpleJdbcInsert.executeAndReturnKey(director.toMap()).longValue());

        return getById(director.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public Director update(Director director) {
        String sql = "UPDATE directors SET name = ? WHERE id = ?";

        jdbcTemplate.update(sql,
                director.getName(),
                director.getId());

        return getById(director.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public void delete(Long id) {
        String sql = "DELETE FROM directors WHERE id = ?";

        jdbcTemplate.update(sql, id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(Long id) {
        String sql = "SELECT CASE WHEN COUNT(id) > 0 THEN true ELSE false END FROM directors WHERE id = ?";

        Boolean exists = jdbcTemplate.queryForObject(sql, Boolean.class, id);

        return exists != null && exists;
    }
}
