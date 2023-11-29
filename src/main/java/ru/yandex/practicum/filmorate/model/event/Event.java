package ru.yandex.practicum.filmorate.model.event;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import ru.yandex.practicum.filmorate.model.AbstractEntity;

import java.time.Instant;

/**
 * Событие пользователя.
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class Event extends AbstractEntity {

    Long eventId;
    Instant createdAt;
    Long userId;
    EventType type;
    Operation operation;
    Long entityId;

    public Event(Long userId, EventType type, Operation operation, Long entityId) {
        this.userId = userId;
        this.type = type;
        this.operation = operation;
        this.entityId = entityId;
    }
}
