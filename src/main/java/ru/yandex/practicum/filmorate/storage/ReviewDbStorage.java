package ru.yandex.practicum.filmorate.storage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.User;

/**
 * DAO для {@link Review}.
 */
@Component
@RequiredArgsConstructor
public class ReviewDbStorage implements ReviewStorage {

    private static final String MAIN_SELECT = "select id, content, is_positive, user_id, film_id, useful from reviews " +
            "where 1 = 1 ";

    private final JdbcTemplate jdbcTemplate;

    @Override
    @Transactional(readOnly = true)
    public Review getById(Long id) {
        String sql = MAIN_SELECT + "and id = ?";

       return jdbcTemplate.queryForObject(sql, ReviewDbStorage::toReview, id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Review> getAll() {
        String sql = MAIN_SELECT + "order by id";

        return jdbcTemplate.query(sql, ReviewDbStorage::toReview);
    }

    @Override
    @Transactional
    public Review create(Review item) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reviews")
                .usingGeneratedKeyColumns("id");

        item.setId(simpleJdbcInsert.executeAndReturnKey(toMap(item)).longValue());

        return getById(item.getId());
    }

    @Override
    @Transactional
    public Review update(Review item) {
        String sql = "update reviews set content = ?, is_positive = ? where id = ?";

        jdbcTemplate.update(sql,
                item.getContent(),
                item.getIsPositive(),
                item.getId());

        return getById(item.getId());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        String sql = "delete from reviews where id = ?";

        jdbcTemplate.update(sql, id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(Long id) {
        String sql = "select exists(select id from reviews where id = ?)";
        Boolean exists = jdbcTemplate.queryForObject(sql, Boolean.class, id);

        return exists != null && exists;
    }

    @Override
    @Transactional
    public void recalculateUseful(Long id, Integer reaction) {
        String sql = "update reviews set useful = useful + ? where id = ?";

        jdbcTemplate.update(sql, reaction, id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Review> getByFilmId(Long filmId, Integer count) {
        String filter = "";
        if (filmId != null) {
            filter = String.format("and film_id = %s ", filmId);
        }

        String sql = MAIN_SELECT + filter + "order by useful desc limit ?";

        return jdbcTemplate.query(sql, ReviewDbStorage::toReview, count);
    }

    private static Review toReview(ResultSet rs, int rowNum) throws SQLException {
        return Review.builder()
                .id(rs.getLong("id"))
                .content(rs.getString("content"))
                .isPositive(rs.getBoolean("is_positive"))
                .film(Film.builder().id(rs.getLong("film_id")).build())
                .user(User.builder().id(rs.getLong("user_id")).build())
                .useful(rs.getInt("useful"))
                .build();
    }

    private Map<String, Object> toMap(Review review) {
        Map<String, Object> values = new HashMap<>();
        values.put("id", review.getId());
        values.put("content", review.getContent());
        values.put("is_positive", review.getIsPositive());
        values.put("user_id", review.getUser().getId());
        values.put("film_id", review.getFilm().getId());
        values.put("useful", 0);
        return values;
    }
}