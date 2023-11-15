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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тесты для {@link FilmGenreDbStorage}.
 */
@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FilmGenreDbStorageTest {

    private final JdbcTemplate jdbcTemplate;

    private FilmStorage filmStorage;

    private FilmGenreDbStorage filmGenreStorage;

    private Film film;

    @BeforeEach
    void setUp() {
        filmStorage = new FilmDbStorage(jdbcTemplate);
        filmGenreStorage = new FilmGenreDbStorage(jdbcTemplate);

        film = Film.builder()
                .id(1L)
                .name("Oppenheimer")
                .description("Epic biographical thriller film written and directed by Christopher Nolan")
                .duration(180)
                .releaseDate(LocalDate.of(2023, 7, 19))
                .mpa(RatingMpa.builder().id(1L).build())
                .genres(Set.of(Genre.builder().id(1L).build(), Genre.builder().id(2L).build()))
                .build();
    }

    @Test
    void addGenresToFilm() {
        filmStorage.create(film);
        filmGenreStorage.addGenresToFilm(film.getId(), film.getGenres());

        film.getGenres().forEach(genre ->
                assertTrue(filmGenreStorage.exists(film.getId(), genre.getId())));
    }

    @Test
    void deleteGenresFromFilm() {
        filmStorage.create(film);
        filmGenreStorage.addGenresToFilm(film.getId(), film.getGenres());
        filmGenreStorage.deleteGenresFromFilm(film.getId());

        film.getGenres().forEach(genre ->
                assertFalse(filmGenreStorage.exists(film.getId(), genre.getId())));
    }
}