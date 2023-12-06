package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * DAO для лайков отзывов.
 */
@Component
@RequiredArgsConstructor
public class ReviewLikeDbStorage {

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void addReactionToReview(Long reviewId, Long userId, Integer reaction) {
        jdbcTemplate.update("merge into review_likes (review_id, user_id, reaction) values (?, ?, ?)",
                reviewId, userId, reaction);
    }

    @Transactional
    public void deleteReactionFromReview(Long reviewId, Long userId, Integer reaction) {
        jdbcTemplate.update("delete from review_likes where review_id = ? and user_id = ? and reaction = ?",
                reviewId, userId, reaction);
    }

    @Transactional(readOnly = true)
    public boolean exists(Long reviewId, Long userId) {
        String sql = "select exists(select user_id from review_likes where review_id = ? and user_id = ?)";

        Boolean exists = jdbcTemplate.queryForObject(sql, Boolean.class, reviewId, userId);

        return exists != null && exists;
    }
}