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
 * Тесты для {@link FriendDbStorage}.
 */
@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FriendDbStorageTest {

    private final JdbcTemplate jdbcTemplate;

    private FriendDbStorage friendStorage;

    private UserStorage userStorage;

    private User firstUser;

    private User secondUser;

    private User thirdUser;

    @BeforeEach
    void setUp() {
        friendStorage = new FriendDbStorage(jdbcTemplate);
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

        thirdUser = User.builder()
                .id(3L)
                .name("Павел")
                .email("pavel@gmail.com")
                .login("man")
                .birthday(LocalDate.of(1996, 7, 11))
                .build();
    }

    @Test
    void getByUserId() {
        userStorage.create(firstUser);
        userStorage.create(secondUser);

        friendStorage.addFriendToUser(firstUser.getId(), secondUser.getId());

        List<User> friends = friendStorage.getByUserId(firstUser.getId());

        assertThat(friends)
                .isNotNull()
                .isNotEmpty()
                .size().isEqualTo(1);

        assertThat(friends.get(0))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(secondUser);
    }

    @Test
    void addFriendToUser() {
        userStorage.create(firstUser);
        userStorage.create(secondUser);

        friendStorage.addFriendToUser(firstUser.getId(), secondUser.getId());

        assertTrue(friendStorage.exists(firstUser.getId(), secondUser.getId()));
    }

    @Test
    void deleteFriendFromUser() {
        userStorage.create(firstUser);
        userStorage.create(secondUser);

        friendStorage.addFriendToUser(firstUser.getId(), secondUser.getId());
        friendStorage.deleteFriendFromUser(firstUser.getId(), secondUser.getId());

        assertFalse(friendStorage.exists(firstUser.getId(), secondUser.getId()));
    }

    @Test
    void getCommonFriends() {
        userStorage.create(firstUser);
        userStorage.create(secondUser);
        userStorage.create(thirdUser);

        friendStorage.addFriendToUser(firstUser.getId(), thirdUser.getId());
        friendStorage.addFriendToUser(secondUser.getId(), thirdUser.getId());

        List<User> commonFriends = friendStorage.getCommonFriends(firstUser.getId(), secondUser.getId());

        assertThat(commonFriends)
                .isNotNull()
                .isNotEmpty()
                .size().isEqualTo(1);

        assertThat(commonFriends.get(0))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(thirdUser);
    }
}