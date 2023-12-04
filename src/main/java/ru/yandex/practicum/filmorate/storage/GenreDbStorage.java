package ru.yandex.practicum.filmorate.storage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.model.Genre;

/**
 * DAO для {@link Genre}.
 */
@Component
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {
    private static final String MAIN_SELECT = "select id, name from genres ";

    private final JdbcTemplate jdbcTemplate;

    @Override
    @Transactional(readOnly = true)
    public Genre getById(Long id) {
        String sql = MAIN_SELECT + "where id = ?";

        return jdbcTemplate.queryForObject(sql, GenreDbStorage::toGenre, id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Genre> getAll() {
        String sql = MAIN_SELECT + "order by id";

        return jdbcTemplate.query(sql, GenreDbStorage::toGenre);
    }

    @Override
    public Genre create(Genre item) {
        throw new UnsupportedOperationException("Операция не разрешена");
    }

    @Override
    public Genre update(Genre item) {
        throw new UnsupportedOperationException("Операция не разрешена");
    }

    @Override
    public void delete(Long id) {
        throw new UnsupportedOperationException("Операция не разрешена");
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(Long id) {
        String sql = "select exists(select id from genres where id = ?)";

        Boolean exists = jdbcTemplate.queryForObject(sql, Boolean.class, id);

        return exists != null && exists;
    }

    static Genre toGenre(ResultSet rs, int rowNum) throws SQLException {
        return Genre.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .build();
    }

    private Map<String, Object> toMap(Genre genre) {
        Map<String, Object> values = new HashMap<>();
        values.put("id", genre.getId());
        values.put("name", genre.getName());

        return values;
    }
}
