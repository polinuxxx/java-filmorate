package ru.yandex.practicum.filmorate.storage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.RatingMpa;

/**
 * DAO для {@link RatingMpa}.
 */
@Component
@RequiredArgsConstructor
public class RatingMpaDbStorage implements RatingMpaStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public RatingMpa getById(Long id) {
        String sql = "select id, name, description from rating_mpa where id = ?";

        List<RatingMpa> mpa = jdbcTemplate.query(sql, RatingMpaDbStorage::toMpa, id);

        return mpa.get(0);
    }

    @Override
    public List<RatingMpa> getAll() {
        String sql = "select id, name, description from rating_mpa order by id";

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
    public boolean exists(Long id) {
        String sql = "select case when count(id) > 0 then true else false end from rating_mpa where id = ?";
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
}
