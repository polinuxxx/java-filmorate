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
import ru.yandex.practicum.filmorate.model.RatingMpa;

/**
 * DAO для {@link RatingMpa}.
 */
@Component
@RequiredArgsConstructor
public class RatingMpaDbStorage implements RatingMpaStorage {
    private static final String MAIN_SELECT = "select id, name, description from rating_mpa ";

    private final JdbcTemplate jdbcTemplate;

    @Override
    @Transactional(readOnly = true)
    public RatingMpa getById(Long id) {
        String sql = MAIN_SELECT + "where id = ?";

        return jdbcTemplate.queryForObject(sql, RatingMpaDbStorage::toMpa, id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RatingMpa> getAll() {
        String sql = MAIN_SELECT + "order by id";

        return jdbcTemplate.query(sql, RatingMpaDbStorage::toMpa);
    }

    @Override
    public RatingMpa create(RatingMpa item) {
        throw new UnsupportedOperationException("Операция не разрешена");
    }

    @Override
    public RatingMpa update(RatingMpa item) {
        throw new UnsupportedOperationException("Операция не разрешена");
    }

    @Override
    public void delete(Long id) {
        throw new UnsupportedOperationException("Операция не разрешена");
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(Long id) {
        String sql = "select exists(select id from rating_mpa where id = ?)";

        Boolean exists = jdbcTemplate.queryForObject(sql, Boolean.class, id);

        return exists != null && exists;
    }

    static RatingMpa toMpa(ResultSet rs, int rowNum) throws SQLException {
        return RatingMpa.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .build();
    }

    private Map<String, Object> toMap(RatingMpa mpa) {
        Map<String, Object> values = new HashMap<>();
        values.put("id", mpa.getId());
        values.put("name", mpa.getName());
        values.put("description", mpa.getDescription());

        return values;
    }
}
