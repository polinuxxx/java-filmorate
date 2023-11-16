package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.exception.EntityAlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;

/**
 * DAO для лайков фильмов.
 */
@Component
@RequiredArgsConstructor
public class LikeDbStorage {

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void addLikeToFilm(Long filmId, Long userId) {
        if (exists(filmId, userId)) {
            throw new EntityAlreadyExistsException(String.format("Фильму с id = %d уже добавлен лайк от пользователя " +
                    "с id = %d", filmId, userId));
        }

        jdbcTemplate.update("insert into likes (film_id, user_id) values (?, ?)",
                filmId, userId);
    }

    @Transactional
    public void deleteLikeFromFilm(Long filmId, Long userId) {
        if (!exists(filmId, userId)) {
            throw new EntityNotFoundException(String.format("У фильма с id = %d отсутствует лайк от пользователя" +
                    " с id = %d", filmId, userId));
        }

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
