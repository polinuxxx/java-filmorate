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
import ru.yandex.practicum.filmorate.model.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тесты для {@link UserStorage}.
 */
@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserDbStorageTest {

    private final JdbcTemplate jdbcTemplate;

    private UserStorage userStorage;

    private User firstUser;

    private User secondUser;

    @BeforeEach
    void setUp() {
        userStorage = new UserDbStorage(jdbcTemplate);

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
        userStorage.create(firstUser);

        User savedUser = userStorage.getById(firstUser.getId());

        assertThat(firstUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(savedUser);
    }

    @Test
    void getAll() {
        userStorage.create(firstUser);
        userStorage.create(secondUser);

        List<User> users = userStorage.getAll();

        assertThat(users)
                .isNotNull()
                .isNotEmpty()
                .size().isEqualTo(2);

        assertThat(users.get(0))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(firstUser);

        assertThat(users.get(1))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(secondUser);
    }

    @Test
    void create() {
        userStorage.create(firstUser);
        assertTrue(userStorage.exists(firstUser.getId()));
    }

    @Test
    void update() {
        userStorage.create(firstUser);

        firstUser.setName("Яна Константиновна");
        userStorage.update(firstUser);

        User updatedUser = userStorage.getById(firstUser.getId());

        assertThat(firstUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(updatedUser);
    }

    @Test
    void delete() {
        userStorage.create(firstUser);

        userStorage.delete(firstUser.getId());

        assertFalse(userStorage.exists(firstUser.getId()));
    }
}