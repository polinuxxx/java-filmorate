package ru.yandex.practicum.filmorate.storage;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Genre;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Тесты для {@link GenreStorage}.
 */
@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class GenreDbStorageTest {

    private final JdbcTemplate jdbcTemplate;

    private GenreStorage genreStorage;

    private Genre firstGenre;

    private Genre secondGenre;

    @BeforeEach
    void setUp() {

        genreStorage = new GenreDbStorage(jdbcTemplate);

        firstGenre = Genre.builder()
                .id(1L)
                .name("Комедия")
                .build();

        secondGenre = Genre.builder()
                .id(2L)
                .name("Драма")
                .build();
    }

    @Test
    void getById() {
        Genre savedGenre = genreStorage.getById(firstGenre.getId());

        assertThat(savedGenre)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(firstGenre);
    }

    @Test
    void getAll() {
        List<Genre> genres = genreStorage.getAll();

        assertThat(genres)
                .isNotNull()
                .isNotEmpty()
                .size().isEqualTo(6);

        assertThat(genres.get(0))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(firstGenre);

        assertThat(genres.get(1))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(secondGenre);
    }
}