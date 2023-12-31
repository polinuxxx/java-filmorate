package ru.yandex.practicum.filmorate.storage;

import java.time.LocalDate;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.model.User;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тесты для {@link LikeDbStorage}.
 */
@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class LikeDbStorageTest {

    private final JdbcTemplate jdbcTemplate;

    private LikeDbStorage likeStorage;

    private UserStorage userStorage;

    private FilmStorage filmStorage;

    private Film film;

    private User user;

    @BeforeEach
    void setUp() {
        likeStorage = new LikeDbStorage(jdbcTemplate);
        userStorage = new UserDbStorage(jdbcTemplate);
        filmStorage = new FilmDbStorage(jdbcTemplate);

        user = User.builder()
                .id(1L)
                .name("Яна")
                .email("yana@mail.ru")
                .login("girl")
                .birthday(LocalDate.of(1980, 1, 1))
                .build();

        film = Film.builder()
                .id(1L)
                .name("Oppenheimer")
                .description("Epic biographical thriller film written and directed by Christopher Nolan")
                .duration(180)
                .releaseDate(LocalDate.of(2023, 7, 19))
                .mpa(RatingMpa.builder().id(1L).build())
                .genres(Set.of(Genre.builder().id(1L).build()))
                .build();
    }

    @Test
    void addLikeToFilm() {
        userStorage.create(user);
        filmStorage.create(film);

        likeStorage.addLikeToFilm(film.getId(), user.getId());

        assertTrue(likeStorage.exists(film.getId(), user.getId()));
    }

    @Test
    void deleteLikeFromFilm() {
        userStorage.create(user);
        filmStorage.create(film);

        likeStorage.addLikeToFilm(film.getId(), user.getId());
        likeStorage.deleteLikeFromFilm(film.getId(), user.getId());

        assertFalse(likeStorage.exists(film.getId(), user.getId()));
    }
}