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
import ru.yandex.practicum.filmorate.model.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тесты для {@link FilmDirectorDbStorage}.
 */
@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FilmDirectorDbStorageTest {

    private final JdbcTemplate jdbcTemplate;

    private FilmDirectorDbStorage filmDirectorStorage;

    private DirectorStorage directorStorage;

    private FilmStorage filmStorage;

    private Film film;

    private Director director;

    @BeforeEach
    void setUp() {
        filmDirectorStorage = new FilmDirectorDbStorage(jdbcTemplate);
        directorStorage = new DirectorDbStorage(jdbcTemplate);
        filmStorage = new FilmDbStorage(jdbcTemplate);

        director = Director.builder()
                .id(1L)
                .name("Chris Columbus")
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
    void addDirectorToFilm() {
        directorStorage.create(director);
        filmStorage.create(film);

        filmDirectorStorage.addDirectorToFilm(film.getId(), director.getId());

        assertTrue(filmDirectorStorage.exists(film.getId(), director.getId()));
    }

    @Test
    void deleteDirectorsFromFilm() {
        directorStorage.create(director);
        filmStorage.create(film);

        filmDirectorStorage.addDirectorToFilm(film.getId(), director.getId());
        filmDirectorStorage.deleteDirectorsFromFilm(film.getId());

        assertFalse(filmDirectorStorage.exists(film.getId(), director.getId()));
    }
}