package ru.yandex.practicum.filmorate.storage;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.RatingMpa;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Тесты для {@link RatingMpaStorage}.
 */
@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class RatingMpaDbStorageTest {

    private final JdbcTemplate jdbcTemplate;

    private RatingMpaStorage mpaStorage;

    private RatingMpa firstMpa;

    private RatingMpa secondMpa;

    @BeforeEach
    void setUp() {

        mpaStorage = new RatingMpaDbStorage(jdbcTemplate);

        firstMpa = RatingMpa.builder()
                .id(1L)
                .name("G")
                .description("У фильма нет возрастных ограничений")
                .build();

        secondMpa = RatingMpa.builder()
                .id(2L)
                .name("PG")
                .description("Детям рекомендуется смотреть фильм с родителями")
                .build();
    }

    @Test
    void getById() {
        RatingMpa savedMpa = mpaStorage.getById(firstMpa.getId());

        assertThat(savedMpa)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(firstMpa);
    }

    @Test
    void getAll() {
        List<RatingMpa> mpa = mpaStorage.getAll();

        assertThat(mpa)
                .isNotNull()
                .isNotEmpty()
                .size().isEqualTo(5);

        assertThat(mpa.get(0))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(firstMpa);

        assertThat(mpa.get(1))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(secondMpa);
    }
}