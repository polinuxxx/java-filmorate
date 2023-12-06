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
import ru.yandex.practicum.filmorate.model.event.Event;
import ru.yandex.practicum.filmorate.model.event.EventType;
import ru.yandex.practicum.filmorate.model.event.Operation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Тесты для {@link EventStorage}.
 */
@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class EventStorageTest {

    private final JdbcTemplate jdbcTemplate;

    private EventStorage eventStorage;

    private UserStorage userStorage;

    private Event firstEvent;

    private Event secondEvent;

    private User user;

    @BeforeEach
    void setUp() {
        eventStorage = new EventDbStorage(jdbcTemplate);
        userStorage = new UserDbStorage(jdbcTemplate);

        user = User.builder()
                .id(1L)
                .name("Яна")
                .email("yana@mail.ru")
                .login("girl")
                .birthday(LocalDate.of(1980, 1, 1))
                .build();

        firstEvent = Event.builder()
                .id(1L)
                .operation(Operation.ADD)
                .type(EventType.FRIEND)
                .user(user)
                .entityId(1L)
                .build();

        secondEvent = Event.builder()
                .id(2L)
                .operation(Operation.REMOVE)
                .type(EventType.FRIEND)
                .user(user)
                .entityId(1L)
                .build();
    }

    @Test
    void create() {
        userStorage.create(user);
        eventStorage.create(firstEvent);

        List<Event> events = eventStorage.getEventsByUserid(user.getId(), 1);

        assertThat(firstEvent)
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("createdAt", "user")
                .isEqualTo(events.get(0));
        assertEquals(user.getId(), events.get(0).getUser().getId());
    }

    @Test
    void getEventsByUserid() {
        userStorage.create(user);
        eventStorage.create(firstEvent);
        eventStorage.create(secondEvent);

        List<Event> events = eventStorage.getEventsByUserid(user.getId(), 2);

        assertThat(events)
                .isNotNull()
                .isNotEmpty()
                .size().isEqualTo(2);

        assertThat(firstEvent)
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("createdAt", "user")
                .isEqualTo(events.get(0));
        assertEquals(user.getId(), events.get(0).getUser().getId());

        assertThat(secondEvent)
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("createdAt", "user")
                .isEqualTo(events.get(1));
        assertEquals(user.getId(), events.get(1).getUser().getId());
    }
}