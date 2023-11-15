package ru.yandex.practicum.filmorate.storage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

/**
 * DAO для {@link Genre}.
 */
@Component
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Genre getById(Long id) {
        String sql = "select id, name from genres where id = ?";

        List<Genre> genres = jdbcTemplate.query(sql, GenreDbStorage::toGenre, id);

        return genres.get(0);
    }

    @Override
    public List<Genre> getAll() {
        String sql = "select id, name from genres order by id";

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
    public boolean exists(Long id) {
        String sql = "select case when count(id) > 0 then true else false end from genres where id = ?";

        Boolean exists = jdbcTemplate.queryForObject(sql, Boolean.class, id);

        return exists != null && exists;
    }

    static Genre toGenre(ResultSet rs, int rowNum) throws SQLException {
        return Genre.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .build();
    }
}
