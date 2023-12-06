package ru.yandex.practicum.filmorate.storage;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для {@link ReviewStorage}.
 */
@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReviewStorageTest {

    private final JdbcTemplate jdbcTemplate;

    private ReviewStorage reviewStorage;

    private FilmStorage filmStorage;

    private UserStorage userStorage;

    private Review firstReview;

    private Review secondReview;

    private User firstUser;

    private User secondUser;

    private Film film;

    @BeforeEach
    void setUp() {
        reviewStorage = new ReviewDbStorage(jdbcTemplate);
        filmStorage = new FilmDbStorage(jdbcTemplate);
        userStorage = new UserDbStorage(jdbcTemplate);

        film = Film.builder()
                .id(1L)
                .name("Oppenheimer")
                .description("Epic biographical thriller film written and directed by Christopher Nolan")
                .duration(180)
                .releaseDate(LocalDate.of(2023, 7, 19))
                .mpa(RatingMpa.builder().id(1L).name("G").description("У фильма нет возрастных ограничений").build())
                .rate(0.0)
                .build();

        firstUser = User.builder()
                .id(1L)
                .name("Яна")
                .email("yana@mail.ru")
                .login("girl")
                .birthday(LocalDate.of(1980, 1, 1))
                .build();

        secondUser = User.builder()
                .id(2L)
                .name("Игорь")
                .email("igor@gmail.com")
                .login("boy")
                .birthday(LocalDate.of(2003, 5, 23))
                .build();

        firstReview = Review.builder()
                .id(1L)
                .content("The film was amazing")
                .user(firstUser)
                .film(film)
                .isPositive(true)
                .useful(0)
                .build();

        secondReview = Review.builder()
                .id(2L)
                .content("The film was awful")
                .user(secondUser)
                .film(film)
                .isPositive(false)
                .useful(0)
                .build();
    }

    @Test
    void getById() {
        filmStorage.create(film);
        userStorage.create(firstUser);
        reviewStorage.create(firstReview);

        Review savedReview = reviewStorage.getById(firstReview.getId());

        assertThat(firstReview)
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("film", "user")
                .isEqualTo(savedReview);
        assertEquals(firstUser.getId(), savedReview.getUser().getId());
        assertEquals(film.getId(), savedReview.getFilm().getId());
    }

    @Test
    void getAll() {
        filmStorage.create(film);
        userStorage.create(firstUser);
        userStorage.create(secondUser);
        reviewStorage.create(firstReview);
        reviewStorage.create(secondReview);

        List<Review> reviews = reviewStorage.getAll();

        assertThat(reviews)
                .isNotNull()
                .isNotEmpty()
                .size().isEqualTo(2);

        assertThat(reviews.get(0))
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("film", "user")
                .isEqualTo(firstReview);
        assertEquals(firstUser.getId(), reviews.get(0).getUser().getId());
        assertEquals(film.getId(), reviews.get(0).getFilm().getId());

        assertThat(reviews.get(1))
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("film", "user")
                .isEqualTo(secondReview);
        assertEquals(secondUser.getId(), reviews.get(1).getUser().getId());
        assertEquals(film.getId(), reviews.get(1).getFilm().getId());
    }

    @Test
    void create() {
        filmStorage.create(film);
        userStorage.create(firstUser);

        reviewStorage.create(firstReview);
        assertTrue(reviewStorage.exists(firstReview.getId()));
    }

    @Test
    void update() {
        filmStorage.create(film);
        userStorage.create(firstUser);
        reviewStorage.create(firstReview);

        firstReview.setContent("The film was touching");
        reviewStorage.update(firstReview);

        Review updatedreview = reviewStorage.getById(firstReview.getId());

        assertThat(firstReview)
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("film", "user")
                .isEqualTo(updatedreview);
        assertEquals(firstUser.getId(), updatedreview.getUser().getId());
        assertEquals(film.getId(), updatedreview.getFilm().getId());
    }

    @Test
    void delete() {
        filmStorage.create(film);
        userStorage.create(firstUser);
        reviewStorage.create(firstReview);

        reviewStorage.delete(firstReview.getId());

        assertFalse(reviewStorage.exists(firstReview.getId()));
    }

    @Test
    void getByFilmId() {
        filmStorage.create(film);
        userStorage.create(firstUser);
        userStorage.create(secondUser);
        reviewStorage.create(firstReview);
        reviewStorage.create(secondReview);

        List<Review> reviews = reviewStorage.getByFilmId(film.getId(), 10);

        assertThat(reviews)
                .isNotNull()
                .isNotEmpty()
                .size().isEqualTo(2);

        assertThat(reviews.get(0))
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("film", "user")
                .isEqualTo(firstReview);
        assertEquals(firstUser.getId(), reviews.get(0).getUser().getId());
        assertEquals(film.getId(), reviews.get(0).getFilm().getId());

        assertThat(reviews.get(1))
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("film", "user")
                .isEqualTo(secondReview);
        assertEquals(secondUser.getId(), reviews.get(1).getUser().getId());
        assertEquals(film.getId(), reviews.get(1).getFilm().getId());
    }
}