package ru.yandex.practicum.filmorate.storage;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.User;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тесты для {@link ReviewLikeDbStorage}.
 */
@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReviewLikeDbStorageTest {

    private final JdbcTemplate jdbcTemplate;

    private ReviewStorage reviewStorage;

    private ReviewLikeDbStorage reviewLikeStorage;

    private FilmStorage filmStorage;

    private UserStorage userStorage;

    private Review review;

    private User user;

    private Film film;

    @BeforeEach
    void setUp() {
        reviewStorage = new ReviewDbStorage(jdbcTemplate);
        filmStorage = new FilmDbStorage(jdbcTemplate);
        userStorage = new UserDbStorage(jdbcTemplate);
        reviewLikeStorage = new ReviewLikeDbStorage(jdbcTemplate);

        film = Film.builder()
                .id(1L)
                .name("Oppenheimer")
                .description("Epic biographical thriller film written and directed by Christopher Nolan")
                .duration(180)
                .releaseDate(LocalDate.of(2023, 7, 19))
                .mpa(RatingMpa.builder().id(1L).name("G").description("У фильма нет возрастных ограничений").build())
                .rate(0.0)
                .build();

        user = User.builder()
                .id(1L)
                .name("Яна")
                .email("yana@mail.ru")
                .login("girl")
                .birthday(LocalDate.of(1980, 1, 1))
                .build();

        review = Review.builder()
                .id(1L)
                .content("The film was amazing")
                .user(user)
                .film(film)
                .isPositive(true)
                .useful(0)
                .build();
    }

    @Test
    void addReactionToReview() {
        filmStorage.create(film);
        userStorage.create(user);
        reviewStorage.create(review);

        reviewLikeStorage.addReactionToReview(review.getId(), user.getId(), 1);
        assertTrue(reviewLikeStorage.exists(review.getId(), user.getId()));
    }

    @Test
    void deleteReactionFromReview() {
        filmStorage.create(film);
        userStorage.create(user);
        reviewStorage.create(review);

        reviewLikeStorage.addReactionToReview(review.getId(), user.getId(), 1);
        reviewLikeStorage.deleteReactionFromReview(review.getId(), user.getId(), 1);
        assertFalse(reviewLikeStorage.exists(review.getId(), user.getId()));
    }
}