package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * DAO для оценок фильмов.
 */
@Component
@RequiredArgsConstructor
public class FilmMarkDbStorage {

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void addMarkToFilm(Long filmId, Long userId, Integer mark) {
        jdbcTemplate.update("merge into film_marks (film_id, user_id, mark) values (?, ?, ?)",
                filmId, userId, mark);
    }

    @Transactional
    public void deleteMarkFromFilm(Long filmId, Long userId) {
        jdbcTemplate.update("delete from film_marks where film_id = ? and user_id = ?", filmId, userId);
    }

    @Transactional(readOnly = true)
    public boolean exists(Long filmId, Long userId) {
        String sql = "select exists(select film_id from film_marks where film_id = ? and user_id = ?)";

        Boolean exists = jdbcTemplate.queryForObject(sql, Boolean.class, filmId, userId);

        return exists != null && exists;
    }
}
