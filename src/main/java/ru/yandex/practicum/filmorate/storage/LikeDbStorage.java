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
}
