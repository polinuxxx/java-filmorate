package ru.yandex.practicum.filmorate.storage;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Director;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тесты для {@link DirectorStorage}.
 */
@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class DirectorDbStorageTest {

    private final JdbcTemplate jdbcTemplate;

    private DirectorStorage directorStorage;

    private Director firstDirector;

    private Director secondDirector;

    @BeforeEach
    void setUp() {

        directorStorage = new DirectorDbStorage(jdbcTemplate);

        firstDirector = Director.builder()
                .id(1L)
                .name("Chris Columbus")
                .build();

        secondDirector = Director.builder()
                .id(2L)
                .name("Wes Anderson")
                .build();
    }

    @Test
    void getById() {
        directorStorage.create(firstDirector);
        Director savedDirector = directorStorage.getById(firstDirector.getId());

        assertThat(savedDirector)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(firstDirector);
    }

    @Test
    void getAll() {
        directorStorage.create(firstDirector);
        directorStorage.create(secondDirector);

        List<Director> directors = directorStorage.getAll();

        assertThat(directors)
                .isNotNull()
                .isNotEmpty()
                .size().isEqualTo(2);

        assertThat(directors.get(0))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(firstDirector);

        assertThat(directors.get(1))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(secondDirector);
    }

    @Test
    void create() {
        Director savedDirector = directorStorage.create(firstDirector);

        assertTrue(directorStorage.exists(firstDirector.getId()));
        assertThat(savedDirector)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(firstDirector);
    }

    @Test
    void update() {
        directorStorage.create(firstDirector);

        firstDirector.setName("Christopher Nolan");
        directorStorage.update(firstDirector);

        Director updatedDirector = directorStorage.getById(firstDirector.getId());

        assertThat(firstDirector)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(updatedDirector);
    }

    @Test
    void delete() {
        directorStorage.create(firstDirector);

        directorStorage.delete(firstDirector.getId());

        assertFalse(directorStorage.exists(firstDirector.getId()));
    }
}