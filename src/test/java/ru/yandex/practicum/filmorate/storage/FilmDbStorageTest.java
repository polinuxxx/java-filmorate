package ru.yandex.practicum.filmorate.storage;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тесты для {@link FilmStorage}.
 */
@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FilmDbStorageTest {

    private final JdbcTemplate jdbcTemplate;

    private FilmStorage filmStorage;

    private UserStorage userStorage;

    private FilmMarkDbStorage markStorage;

    private FilmGenreDbStorage filmGenreStorage;

    private FilmDirectorDbStorage filmDirectorStorage;

    private DirectorStorage directorStorage;

    private User firstUser;

    private User secondUser;

    private Film firstFilm;

    private Film secondFilm;

    private Director firstDirector;

    private Director secondDirector;

    @BeforeEach
    void setUp() {
        filmStorage = new FilmDbStorage(jdbcTemplate);
        userStorage = new UserDbStorage(jdbcTemplate);
        markStorage = new FilmMarkDbStorage(jdbcTemplate);
        filmGenreStorage = new FilmGenreDbStorage(jdbcTemplate);
        filmDirectorStorage = new FilmDirectorDbStorage(jdbcTemplate);
        directorStorage = new DirectorDbStorage(jdbcTemplate);

        firstDirector = Director.builder()
                .id(1L)
                .name("Chris Columbus")
                .build();

        secondDirector = Director.builder()
                .id(2L)
                .name("Wes Anderson")
                .build();

        firstFilm = Film.builder()
                .id(1L)
                .name("Oppenheimer")
                .description("Epic biographical thriller film written and directed by Christopher Nolan")
                .duration(180)
                .releaseDate(LocalDate.of(2023, 7, 19))
                .mpa(RatingMpa.builder().id(1L).name("G").description("У фильма нет возрастных ограничений").build())
                .genres(Set.of(Genre.builder().id(1L).name("Комедия").build(),
                        Genre.builder().id(2L).name("Драма").build()))
                .directors(Set.of(firstDirector))
                .rate(0.0)
                .build();

        secondFilm = Film.builder()
                .id(2L)
                .name("Barbie")
                .description("Fantasy comedy film directed by Greta Gerwig from a screenplay she wrote with Noah Baumbach")
                .duration(120)
                .releaseDate(LocalDate.of(2023, 7, 9))
                .mpa(RatingMpa.builder().id(1L).name("G").description("У фильма нет возрастных ограничений").build())
                .genres(Set.of(Genre.builder().id(1L).name("Комедия").build()))
                .directors(Set.of(firstDirector, secondDirector))
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
    }

    @Test
    void getById() {
        directorStorage.create(firstDirector);
        filmStorage.create(firstFilm);
        filmGenreStorage.addGenresToFilm(firstFilm.getId(), firstFilm.getGenres());
        filmDirectorStorage.addDirectorToFilm(firstFilm.getId(), firstFilm.getDirectors());

        Film savedFilm = filmStorage.getById(firstFilm.getId());

        assertThat(firstFilm)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(savedFilm);
    }

    @Test
    void getAll() {
        directorStorage.create(firstDirector);
        directorStorage.create(secondDirector);
        filmStorage.create(firstFilm);
        filmGenreStorage.addGenresToFilm(firstFilm.getId(), firstFilm.getGenres());
        filmDirectorStorage.addDirectorToFilm(firstFilm.getId(), firstFilm.getDirectors());
        filmStorage.create(secondFilm);
        filmGenreStorage.addGenresToFilm(secondFilm.getId(), secondFilm.getGenres());
        filmDirectorStorage.addDirectorToFilm(secondFilm.getId(), secondFilm.getDirectors());

        List<Film> films = filmStorage.getAll();

        assertThat(films)
                .isNotNull()
                .isNotEmpty()
                .size().isEqualTo(2);

        assertThat(films.get(0))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(firstFilm);

        assertThat(films.get(1))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(secondFilm);
    }

    @Test
    void create() {
        directorStorage.create(firstDirector);
        filmStorage.create(firstFilm);
        filmGenreStorage.addGenresToFilm(firstFilm.getId(), firstFilm.getGenres());
        filmDirectorStorage.addDirectorToFilm(firstFilm.getId(), firstFilm.getDirectors());

        Film savedFilm = filmStorage.getById(firstFilm.getId());

        assertTrue(filmStorage.exists(firstFilm.getId()));
        assertThat(savedFilm)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(firstFilm);
    }

    @Test
    void update() {
        directorStorage.create(firstDirector);

        filmStorage.create(firstFilm);
        filmGenreStorage.addGenresToFilm(firstFilm.getId(), firstFilm.getGenres());
        filmDirectorStorage.addDirectorToFilm(firstFilm.getId(), firstFilm.getDirectors());

        firstFilm.setName("Barbie girl");
        filmStorage.update(firstFilm);

        Film updatedFilm = filmStorage.getById(firstFilm.getId());

        assertThat(firstFilm)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(updatedFilm);
    }

    @Test
    void delete() {
        filmStorage.create(firstFilm);

        filmStorage.delete(firstFilm.getId());

        assertFalse(filmStorage.exists(firstFilm.getId()));
    }
}