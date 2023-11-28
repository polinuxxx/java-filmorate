package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;

/**
 * DAO для лайков отзывов.
 */
@Component
@RequiredArgsConstructor
public class ReviewLikeDbStorage {

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void addLikeToReview(Long reviewId, Long userId, Integer reaction) {

        jdbcTemplate.update("merge into review_likes (review_id, user_id, reaction) values (?, ?, ?)",
                reviewId, userId, reaction);
    }

    @Transactional
    public void deleteLikeFromReview(Long reviewId, Long userId, Integer reaction) {
        if (!exists(reviewId, userId, reaction)) {
            throw new EntityNotFoundException(String.format("У фильма с id = %d отсутствует реакция от пользователя" +
                    " с id = %d", reviewId, userId));
        }

        jdbcTemplate.update("delete from review_likes where review_id = ? and user_id = ? and reaction = ?",
                reviewId, userId, reaction);
    }

    @Transactional(readOnly = true)
    public boolean exists(Long reviewId, Long userId, Integer reaction) {
        String sql = "select case when count(review_id) > 0 then true else false end " +
                "from review_likes where review_id = ? and user_id = ? and reaction = ?";
        Boolean exists = jdbcTemplate.queryForObject(sql, Boolean.class, reviewId, userId, reaction);

        return exists != null && exists;
    }
}