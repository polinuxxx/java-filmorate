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
    public void addLikeToReview(Long reviewId, Long userId, Integer reaction) {
        jdbcTemplate.update("merge into review_likes (review_id, user_id, reaction) values (?, ?, ?)",
                reviewId, userId, reaction);
    }

    @Transactional
    public void deleteLikeFromReview(Long reviewId, Long userId, Integer reaction) {
        jdbcTemplate.update("delete from review_likes where review_id = ? and user_id = ? and reaction = ?",
                reviewId, userId, reaction);
    }
}