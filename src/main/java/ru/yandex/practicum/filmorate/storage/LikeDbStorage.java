package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * DAO для лайков фильмов.
 */
@Component
@RequiredArgsConstructor
public class LikeDbStorage {

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void addLikeToFilm(Long filmId, Long userId) {
        jdbcTemplate.update("merge into likes (film_id, user_id) values (?, ?)",
                filmId, userId);
    }

    @Transactional
    public void deleteLikeFromFilm(Long filmId, Long userId) {
        jdbcTemplate.update("delete from likes where film_id = ? and user_id = ?", filmId, userId);
    }

    @Transactional(readOnly = true)
    public boolean exists(Long filmId, Long userId) {
        String sql = "select case when count(film_id) > 0 then true else false end " +
                "from likes where film_id = ? and user_id = ?";
        Boolean exists = jdbcTemplate.queryForObject(sql, Boolean.class, filmId, userId);

        return exists != null && exists;
    }
}
