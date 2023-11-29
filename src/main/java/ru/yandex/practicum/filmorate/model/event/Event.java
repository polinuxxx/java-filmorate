package ru.yandex.practicum.filmorate.model.event;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import ru.yandex.practicum.filmorate.model.AbstractEntity;
import ru.yandex.practicum.filmorate.model.User;

import java.time.Instant;

/**
 * Событие пользователя.
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class Event extends AbstractEntity {

    Instant createdAt;
    User user;
    EventType type;
    Operation operation;
    Long entityId;
}
